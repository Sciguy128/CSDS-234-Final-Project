package csds234_final_project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class Main 
{
    private static DirectedGraph<String, DefaultEdge> graph;

    public static void main(String[] args) throws FileNotFoundException 
    {
        List<Video> videos = parseFile("0222", 0);
        graph = createGraph(videos);
    }

    //Create graph of video IDs connected by their related videos
    private static DirectedGraph<String, DefaultEdge> createGraph (List<Video> videos)
    {
        DirectedGraph<String, DefaultEdge> out = new DefaultDirectedGraph<>(DefaultEdge.class);

        //Add all vertices and edges
        for (Video vid : videos) 
        {  
            //Make sure vertex does not already exist before adding vertex
            if (!out.vertexSet().contains(vid.getId())) { out.addVertex(vid.getId()); }
            //Add edges
            for (String rel : vid.getRelated()) 
            { 
                //Make sure other end of edge exists before adding edge
                if (!out.vertexSet().contains(rel)) { out.addVertex(rel); }
                out.addEdge(vid.getId(), rel); 
            }
        }

        return out;
    }

    //Parses multiple files in a dataset
    private static List<Video> parseDataset (String dataset) throws FileNotFoundException
    {
        List<Video> out = new ArrayList<Video>();
        int count = 0;

        //Loop through ints until file not found
        while (true) 
        {
            try { out.addAll(parseFile(dataset, count)); } 
            catch (FileNotFoundException e) { break; }
            count++;
        }

        return out;
    }

    //Parses tab-seperated values into Video objects
    private static List<Video> parseFile (String dataset, int fileNum) throws FileNotFoundException
    {
        List<Video> out = new ArrayList<Video>();
        //Data stored in "data" folder, each dataset is a folder containing int-named .txt files
        File file = new File(".\\data\\" + dataset + "\\" + fileNum + ".txt");
        Scanner scan = new Scanner(file);

        while (scan.hasNextLine()) 
        {
            //Split line into values
            String[] data = scan.nextLine().split("\t");
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
            //Get related video IDs
            String[] related = new String[data.length - 9];
            for (int i = 9; i < data.length; i++) 
            {
                related[i - 9] = data[i].trim();
            }

            out.add(new Video(id, uploader, age, category, length, views, rate, ratings, comments, related));
        }

        scan.close();
        return out;
    }
}