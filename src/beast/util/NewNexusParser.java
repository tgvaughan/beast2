package beast.util;

import beast.core.util.Log;
import beast.evolution.alignment.*;
import beast.evolution.datatype.DataType;
import beast.evolution.datatype.StandardData;
import beast.evolution.datatype.UserDataType;
import beast.evolution.tree.TraitSet;
import beast.evolution.tree.Tree;
import beast.util.nexusparser.*;
import beast.util.nexusparser.NexusParser;
import beast.util.treeparser.NewickLexer;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NewNexusParser {

    /**
     * Beast II objects reconstructed from the file*
     */
    public Alignment m_alignment;
    public List<Alignment> filteredAlignments = new ArrayList<>();
    public TraitSet traitSet;

    public List<String> taxa;
    public List<Tree> trees;

    static Set<String> g_sequenceIDs;

    public Map<String, String> translationMap = null;

    static {
        g_sequenceIDs = new HashSet<>();
    }

    public List<TaxonSet> taxonsets = new ArrayList<>();

    private List<NexusParserListener> listeners = new ArrayList<>();

    /**
     * Adds a listener for client classes that want to monitor progress of the parsing.
     *
     * @param listener
     */
    public void addListener(final NexusParserListener listener) {
        listeners.add(listener);
    }

    /**
     * Try to parse BEAST 2 objects from the given file
     *
     * @param file the file to parse.
     */
    public void parseFile(final File file) throws IOException {
        final String fileName = file.getName().replaceAll(".*[\\/\\\\]", "").replaceAll("\\..*", "");

        parseFile(fileName, new FileReader(file));

    }

    /**
     * try to reconstruct Beast II objects from the given reader
     *
     * @param id     a name to give to the parsed results
     * @param reader a reader to parse from
     *               TODO: RRB: throws IOException now instead of just Exception.
     *               java.text.ParseException seems more appropriate, but requires keeping track of the position in the file, which is non-trivial
     */
    public void parseFile(final String id, final Reader reader) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(reader);

        // Custom parse/lexer error listener
        BaseErrorListener errorListener = new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer,
                                    Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg, RecognitionException e) {
                throw new ParseCancellationException("Error parsing character "
                        + charPositionInLine + " on line "
                        + line + " of  Nexus file: " + msg);
            }
        };

        // Use lexer to produce token stream

        NexusLexer lexer = new NexusLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Parse token stream to produce parse tree

        beast.util.nexusparser.NexusParser parser = new NexusParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        ParseTree parseTree = parser.nexus();

        // Traverse parse tree, constructing BEAST tree along the way

        NexusASTVisitor visitor = new NexusASTVisitor();
        visitor.visit(parseTree);
    }

    class NexusASTVisitor extends NexusBaseVisitor<Void> {

        @Override
        public Void visitData_block(NexusParser.Data_blockContext ctx) {

            if (ctx.dimensions_command().isEmpty())
                throw new ParseCancellationException("Data blocks must contain dimensions specification.");

            for (NexusParser.CommandContext cctx : ctx.command())
                System.out.println(cctx.command_name.getText());

            return null;
        }
    }

    public static void main(String[] args) throws IOException {

        NewNexusParser newNexusParser = new NewNexusParser();
        newNexusParser.parseFile(new File("examples/nexus/47.nex"));
    }
}

