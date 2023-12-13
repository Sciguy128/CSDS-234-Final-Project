package csds234_final_project;//.src.main.java.csds234_final_project;

public class Video 
{
    private String id;
    private String uploader;
    private int age;
    private String category;
    private int length;
    private int views;
    private double rate;
    private int ratings;
    private int comments;
    private String[] related;

    Video (String id, String uploader, int age, String category, int length, 
           int views, double rate, int ratings, int comments, String[] related)
    {
        this.id = id;
        this.uploader = uploader;
        this.age = age;
        this.category = category;
        this.length = length;
        this.views = views;
        this.rate = rate;
        this.ratings = ratings;
        this.comments = comments;
        this.related = related;
    }

    public String getId () { return id; }
    public String getUploader () {  return uploader; }
    public int getAge () {  return age; }
    public String getCategory () {  return category; }
    public int getLength () {  return length; }
    public int getViews () {  return views; }
    public double getRate () {  return rate; }
    public int getRatings () {  return ratings; }
    public int getComments () {  return comments; }
    public String[] getRelated () { return related; }
}
