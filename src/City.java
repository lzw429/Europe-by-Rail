public class City {
    private String name;
    private int total_fee;
    private int total_distance;
    private String from_city;
    private boolean visited;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public int getTotal_distance() {
        return total_distance;
    }

    public void setTotal_distance(int total_distance) {
        this.total_distance = total_distance;
    }

    public String getFrom_city() {
        return from_city;
    }

    public void setFrom_city(String from_city) {
        this.from_city = from_city;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public City(String s) {
        this.name = s;
        total_distance = Integer.MAX_VALUE;
        total_fee = Integer.MAX_VALUE;
    }

    public City() {
        total_distance = Integer.MAX_VALUE;
        total_fee = Integer.MAX_VALUE;
    }
}
