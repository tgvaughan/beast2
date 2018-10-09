package beast.util;

import beast.core.util.Log;
import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.Taxon;
import beast.evolution.alignment.TaxonSet;
import beast.evolution.tree.TraitSet;
import beast.evolution.tree.Tree;
import beast.math.distributions.MRCAPrior;
import beast.util.nexusparser.NexusBaseVisitor;
import beast.util.nexusparser.NexusLexer;
import beast.util.nexusparser.NexusParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;
import java.util.*;

public class NewNexusParser {

     /**
     * Beast II objects reconstructed from the file*
     */
    public Alignment m_alignment;
    public List<Alignment> filteredAlignments = new ArrayList<>();
    public TraitSet traitSet;
    public List<MRCAPrior> calibrations;

    public List<String> taxa;
    List<Taxon> taxonList = new ArrayList<>();
    public List<Tree> trees;

    static Set<String> g_sequenceIDs;

    public Map<String, String> translationMap = null;

    static {
        g_sequenceIDs = new HashSet<>();
    }

    public List<TaxonSet> taxonsets = new ArrayList<>();

    public void parseFile(File file) throws IOException {
        final String fileName = file.getName().replaceAll(".*[\\/\\\\]", "").replaceAll("\\..*", "");
        parseFile(fileName, new FileReader(file));
    }

    public void parseFile(String id, Reader reader) throws IOException {

        CharStream charStream = CharStreams.fromReader(reader);
        NexusLexer lexer = new NexusLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        beast.util.nexusparser.NexusParser parser = new beast.util.nexusparser.NexusParser(tokens);

        ParseTree parseTree = parser.nexus();

        Visitor visitor = new Visitor();
        visitor.visit(parseTree);
    }

    private boolean namesEqual(String name1, String name2) {
        return name1.trim().toLowerCase().equals(name2.trim().toLowerCase());
    }


    protected List<String> getIndexedTranslationMap(final Map<String, String> translationMap, final int origin) {

        Log.warning.println("translation map size = " + translationMap.size());

        final String[] taxa = new String[translationMap.size()];

        for (final String key : translationMap.keySet()) {
            taxa[Integer.parseInt(key) - origin] = translationMap.get(key);
        }
        return Arrays.asList(taxa);
    }

    /**
     * @param translationMap
     * @return minimum key value if keys are a contiguous set of integers starting from zero or one, -1 otherwise
     */
    protected int getIndexedTranslationMapOrigin(final Map<String, String> translationMap) {

        final SortedSet<Integer> indices = new TreeSet<>();

        int count = 0;
        for (final String key : translationMap.keySet()) {
            final int index = Integer.parseInt(key);
            indices.add(index);
            count += 1;
        }
        if ((indices.last() - indices.first() == count - 1) && (indices.first() == 0 || indices.first() == 1)) {
            return indices.first();
        }
        return -1;
    }

    /**
     * @param translateArgs string containing arguments of the translate command
     * @return a map of taxa translations, keys are generally integer node number starting from 1
     *         whereas values are generally descriptive strings.
     * @throws IOException
     */
    protected Map<String, String> parseTranslateCommand(String translateArgs) throws IOException {

        final Map<String, String> translationMap = new HashMap<>();

        final String[] taxaTranslations = translateArgs.toString().split(",");
        for (final String taxaTranslation : taxaTranslations) {
            final String[] translation = taxaTranslation.trim().split("[\t ]+");
            if (translation.length == 2) {
                translationMap.put(translation[0], translation[1]);
            } else {
                Log.warning.println("Ignoring translation:" + Arrays.toString(translation));
            }
        }
        return translationMap;
    }

    private boolean taxonListContains(String taxon) {
        for (Taxon t : taxonList) {
            if (t.getID().equals(taxon)) {
                return true;
            }
        }
        return false;
    }

    class Visitor extends NexusBaseVisitor<Void> {

        @Override
        public Void visitTrees_block(NexusParser.Trees_blockContext ctx) {
            trees = new ArrayList<>();

            int origin = -1;

            // Search for translate command
            if (ctx.translate_command() != null) {
                try {
                    translationMap = parseTranslateCommand(ctx.translate_command().translate_args().getText());
                    origin = getIndexedTranslationMapOrigin(translationMap);
                    if (origin != -1) {
                        taxa = getIndexedTranslationMap(translationMap, origin);
                    }
                } catch (IOException ex) {
                    throw new ParseCancellationException(ex);
                }
            }

            TreeParser treeParser;

            // Read trees
            for (NexusParser.Tree_commandContext cmdContext : ctx.tree_command()) {
                if (origin != -1) {
                    treeParser = new TreeParser(taxa, cmdContext.tree_string().getText(), origin, false);
                } else {
                    try {
                        treeParser = new TreeParser(taxa, cmdContext.tree_string().getText(), 0, false);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        treeParser = new TreeParser(taxa, cmdContext.tree_string().getText(), 1, false);
                    }
                }

                trees.add(treeParser);
            }
            return null;
        }

        @Override
        public Void visitTaxa_block(NexusParser.Taxa_blockContext ctx) {
            taxa = new ArrayList<>();

            int expectedTaxonCount = -1;

            if (ctx.dimensions_command() != null) {
                expectedTaxonCount = Integer.valueOf(ctx.dimensions_command().taxcount().getText());
            }

            if (ctx.taxlabels_command() != null) {
                for (NexusParser.Taxon_nameContext nameCtx : ctx.taxlabels_command().taxon_name()) {
                    String taxonName = nameCtx.getText();
                    if (taxonName.startsWith("'") || taxonName.startsWith("\""))
                        taxonName = taxonName.substring(1,taxonName.length()-1);

                    if (!taxa.contains(taxonName))
                        taxa.add(taxonName);

                    if (!taxonListContains(taxonName))
                        taxonList.add(new Taxon(taxonName));
                }
            }

            if (expectedTaxonCount>=0 && expectedTaxonCount != taxa.size())
                throw new ParseCancellationException("Number of taxa ("
                        + taxa.size()
                        + ") is not equal to number specified in 'dimension' command ("
                        + expectedTaxonCount + ") specified in 'taxa' block.");

            return null;
        }
    }

    public static void main(String[] args) throws IOException {

        NewNexusParser parser = new NewNexusParser();
        parser.parseFile(new File("examples/nexus/angiosperms.nex"));

        System.out.println("Done.");
    }
}
