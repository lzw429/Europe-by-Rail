public class Service {
    private int fee;
    private int distance;
    private String destination;

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Service(String destination, int fee, int distance) {
        this.destination = destination;
        this.fee = fee;
        this.distance = distance;
    }
}
