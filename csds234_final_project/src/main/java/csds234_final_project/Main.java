package csds234_final_project;//.src.main.java.csds234_final_project;

//import org.jgrapht.alg.scoring.KatzCentrality;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.Graph;
//import org.jgrapht.alg.scoring.*;

public class Main 
{
    private static DefaultDirectedGraph<String, DefaultEdge> graph;

    public static void main(String[] args) throws FileNotFoundException 
    {
        //Map<String, Video> videos = parseFile("0222",0);
        Map<String, Video> videos = parseDataset("0222");
        graph = createGraph(videos);
/*
        //This whole part is a way to view the centrality of nodes in the graph
        //Centrality Measure( Depending on what we want to look for, could be Betweenness, Closeness, EdgeBetweenness, Eigenvector, Harmonic, Katz, or PageRank)
        KatzCentrality<String, DefaultEdge> degreeCentrality = new KatzCentrality<>(graph);

        // Calculating centrality scores
        Map<String, Double> scores = degreeCentrality.getScores();

        // Printing the centrality scores
        for (Map.Entry<String, Double> entry : scores.entrySet()) {
            System.out.println("Video ID: " + entry.getKey() + ", Centrality: " + entry.getValue());
        }

        System.out.println(Collections.max(scores.entrySet(), Map.Entry.comparingByValue()).getKey());

        System.out.println(scores.get(Collections.max(scores.entrySet(), Map.Entry.comparingByValue()).getKey()));
        */
    }

    //Create graph of video IDs connected by their related videos
    private static DefaultDirectedGraph<String, DefaultEdge> createGraph (Map<String, Video> videos)
    {
        DefaultDirectedGraph<String, DefaultEdge> out = new DefaultDirectedGraph<>(DefaultEdge.class);

        //Add all vertices and edges
        for (String id : videos.keySet()) 
        {  
            Video vid = videos.get(id);
            //Make sure vertex does not already exist before adding vertex
            if (!out.vertexSet().contains(id)) { out.addVertex(id); }
            //Add edges if they exist
            if (vid.getRelated() != null)
            {
                for (String rel : vid.getRelated()) 
                { 
                    //Make sure other end of edge exists before adding edge
                    if (!out.vertexSet().contains(rel)) { out.addVertex(rel); }
                    out.addEdge(id, rel); 
                }
            }
        }

        return out;
    }


    //Parses multiple files in a dataset
    private static Map<String, Video> parseDataset (String dataset) throws FileNotFoundException
    {
        Map<String, Video> out = new HashMap<String, Video>();
        int count = 0;

        //Loop through ints until file not found
        while (true) 
        {
            try { out.putAll(parseFile(dataset, count)); } 
            catch (FileNotFoundException e) { break; }
            count++;
        }

        return out;
    }

    //Parses tab-seperated values into Video objects
    private static Map<String, Video> parseFile (String dataset, int fileNum) throws FileNotFoundException
    {
        Map<String, Video> out = new HashMap<String, Video>();
        //Data stored in "data" folder, each dataset is a folder containing int-named .txt files
        File file = new File(".\\data\\" + dataset + "\\" + fileNum + ".txt");
        Scanner scan = new Scanner(file);

        while (scan.hasNextLine()) 
        {
            //Split line into values
            String[] data = scan.nextLine().split("\t");
            if (data.length > 8) //Ensure all data is present
            {
                //Get basic data
                String id = data[0].trim();
                String uploader = data[1].trim();
                int age = Integer.parseInt(data[2].trim());
                String category = data[3].trim();
                int length = Integer.parseInt(data[4].trim());
                int views = Integer.parseInt(data[5].trim());
                double rate = Double.parseDouble(data[6].trim());
                int ratings = Integer.parseInt(data[7].trim());
                int comments = Integer.parseInt(data[8].trim());

                String[] related = null;
                if (data.length > 9) //Ensure there are related videos
                {
                    //Get related video IDs
                    related = new String[data.length - 9];
                    for (int i = 9; i < data.length; i++) 
                    {
                        related[i - 9] = data[i].trim();
                    }
                }

                out.put(id, new Video(id, uploader, age, category, length, views, rate, ratings, comments, related));
            }
        }

        scan.close();
        return out;
    }
}