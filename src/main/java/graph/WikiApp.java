package graph;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class WikiApp {
    public static void main(String[] args) {
        WikiGraph currGraph = null;
        Scanner cmdIn = new Scanner(System.in);

        while(true) {
            System.out.print("WikiShell>");
            String line = cmdIn.nextLine();
            String[] lineArgs = line.split(" ");
            if(lineArgs.length == 0) {
                continue;
            }
            switch (lineArgs[0]) {
                case "write" :
                    if(lineArgs.length != 2) {
                        System.out.println("write <filename>");
                        break;
                    }
                    File output = write(lineArgs[1], currGraph);
                    System.out.println(output.getAbsolutePath());
                    break;
                case "create" :
                    if(lineArgs.length == 2) {
                        currGraph = create(lineArgs[1]);
                    } else if(lineArgs.length == 3) {
                        currGraph = create(lineArgs[1], Integer.parseInt(lineArgs[2]));
                    } else {
                        System.out.println("create <page> <depth?>");
                    }
                    break;
                case "load" :
                    if(lineArgs.length != 2) {
                        System.out.println("load <file>");
                        break;
                    }
                    currGraph = load(lineArgs[1]);
                    break;
                case "path" :
                    if(lineArgs.length != 3) {
                        System.out.println("path <from> <to>");
                        break;
                    }
                    System.out.println(path(lineArgs[1], lineArgs[2], currGraph));
                    break;
                case "exit" :
                    System.exit(0);
                default :
                    break;
            }
        }
    }

    public static File write(String filename, WikiGraph graph) {
        if(graph == null) {
            return null;
        }
        File out = new File(filename);
        try (PrintStream writeOut = new PrintStream(out)) {
            graph.write(writeOut);
        } catch (FileNotFoundException e) {
            return null;
        }
        return out;
    }

    public static WikiGraph create(String pagename) {
        WikiGraph g = new WikiGraph();
        g.generateFromStart(new WikiPage(pagename, false));
        return g;
    }
    public static WikiGraph create(String pagename, int depth) {
        WikiGraph g = new WikiGraph();
        g.generateFromStart(new WikiPage(pagename,false), depth);
        return g;
    }

    public static WikiGraph load(String filename) {
        File fin = new File(filename);
        try (Scanner readIn = new Scanner(fin)) {
            return WikiGraph.read(readIn);
        } catch (IOException e) {
        }
        return null;
    }

    public static List<WikiPage> path(String from, String to, WikiGraph graph) {
        return null;
    }
}
