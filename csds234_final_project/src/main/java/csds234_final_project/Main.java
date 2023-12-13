package csds234_final_project.src.main.java.csds234_final_project;

//import org.jgrapht.alg.scoring.KatzCentrality;
import org.jgrapht.alg.scoring.KatzCentrality;
import org.jgrapht.alg.scoring.PageRank;
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
        //Map<String, Video> videos = parseDataset("0222");
        Map<String, Video> video1 = parseFile("0222",0);
        Map<String, Video> video2 = parseFile("0222",1);
        Map<String, Video> video3 = parseFile("0222",2);
        Map<String, Video> video4 = parseFile("0222",3);
        Map<String, Video> video5 = parseFile("0222",4);
        Map<String, Video> video6 = parseFile("0222",5);
        graph = createGraph(video1);
        video1 = null; //here to froce the garabage collector to clean up the heap space
        graph = updateGraph(video2,graph);
        video2 = null;
        graph = updateGraph(video3,graph);
        video3 = null;
        graph = updateGraph(video4,graph);
        video4 = null;
        graph = updateGraph(video5,graph);
        video5 = null;
        //graph = updateGraph(video6,graph);
        video6 = null;
        System.out.println(graph.vertexSet().size());







        PageRank<String, DefaultEdge> pRank = new PageRank<>(graph);

        // Calculating PageRank scores
        Map<String, Double> scores = pRank.getScores();

        // Sort the videos by their PageRank scores in descending order
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(scores.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));


        Map<String, Video> videos = parseDataset("0222");
        // Select the top k videos
        int k = 10; // Set the value of k as required
        List<String> topKVideos = new ArrayList<>();
        for (int i = 0; i < Math.min(k, sortedEntries.size()); i++) {
            Map.Entry<String, Double> entry = sortedEntries.get(i);
            topKVideos.add(entry.getKey());
            System.out.println("Video ID: " + entry.getKey() + ", PageRank: " + entry.getValue() +
                    "\nVideo Info: " + videos.get(entry.getKey()));
        }
    }

    private static DefaultDirectedGraph<String, DefaultEdge> updateGraph (Map<String, Video> videos, DefaultDirectedGraph<String, DefaultEdge> graph)
    {
        DefaultDirectedGraph<String, DefaultEdge> out = graph;

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