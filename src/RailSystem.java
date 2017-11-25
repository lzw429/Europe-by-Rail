import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RailSystem {
    private HashMap<String, List<Service>> outgoing_services;
    private HashMap<String, City> cities;

    private void load_services() {
        // TODO 读取services.txt
        String line;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("data/services.txt"));
            while ((line = reader.readLine()) != null) {
                String item[] = line.split(" ");
                Service service = new Service(item[1], Integer.parseInt(item[2]), Integer.parseInt(item[3]));

                if (outgoing_services.containsKey(item[0])) {
                    outgoing_services.get(item[0]).add(service);
                } else {
                    ArrayList<Service> list = new ArrayList<>();
                    list.add(service);
                    outgoing_services.put(item[0], list);
                }
                if (!cities.containsKey(item[0]))
                    cities.put(item[0], new City(item[0]));
                if (!cities.containsKey(item[1]))
                    cities.put(item[1], new City(item[1]));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reset() {
        //TODO 重置cities数据
        for (Map.Entry<String, City> cityEntry : cities.entrySet()) {
            cityEntry.getValue().setFrom_city("");
            cityEntry.getValue().setTotal_distance(Integer.MAX_VALUE);
            cityEntry.getValue().setTotal_fee(Integer.MAX_VALUE);
            cityEntry.getValue().setVisited(false);
        }
    }

    private Pair<Integer, Integer> calc_route(String start, String destination) {
        if (cities.get(start) == null || cities.get(destination) == null) {
            throw new IllegalArgumentException("输入的地点有误");
        }

        // 重写比较器，通过费用比较
        Comparator<City> distanceComparator = (o1, o2) -> Integer.compare(o1.getTotal_fee() - o2.getTotal_fee(), 0);

        PriorityQueue<City> candidate = new PriorityQueue<>(55, distanceComparator);

        // Dijkstra 最短路径算法
        City cur_city = cities.get(start);
        cur_city.setTotal_distance(0);
        cur_city.setTotal_fee(0);

        candidate.add(cur_city);
        do {
            cur_city = candidate.peek();//peek 即top
            for (Service service : outgoing_services.get(cur_city.getName()))// 遍历cur_city的邻接城市
            {
                City dest_city = cities.get(service.getDestination());
                int curFee = cur_city.getTotal_fee() + service.getFee();
                if (!cur_city.isVisited() && curFee < dest_city.getTotal_fee()) {
                    // 更新费用和里程，dest_city的上一个城市应改为cur_city
                    dest_city.setTotal_distance(cur_city.getTotal_distance() + service.getDistance());
                    dest_city.setTotal_fee(curFee);
                    dest_city.setFrom_city(cur_city.getName());
                    if (!candidate.contains(dest_city)) {
                        candidate.add(dest_city);
                    } else {
                        //重新建堆
                        candidate.remove(dest_city);
                        candidate.add(dest_city);
                    }
                }
            }
            // 遍历完邻接的城市
            cur_city.setVisited(true);
            candidate.poll();
        } while (candidate.size() > 0);

        // 返回总费用与总里程
        if (cities.get(destination).isVisited())// 如果找到最短路径
            return new Pair<>(cities.get(destination).getTotal_fee(), cities.get(destination).getTotal_distance());
        else // 如果未找到路径
            return new Pair<>(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    private String recover_route(String destination) {
        //TODO 函数返回从出发城市到目的城市的最短路径
        City cur_city = cities.get(destination);
        StringBuilder ret = new StringBuilder();
        while (cur_city.getTotal_fee() != 0) {
            ret.insert(0, " to " + cur_city.getName());
            cur_city = cities.get(cur_city.getFrom_city());
        }
        ret.insert(0, cur_city.getName());
        return ret.toString();
    }

    private RailSystem() {
        outgoing_services = new HashMap<>();
        cities = new HashMap<>();
    }

    public static void main(String args[]) {
        RailSystem rs = new RailSystem();
        rs.load_services();
        while (true) {
            rs.reset();
            System.out.println("\n\nEnter a start and destination city: <'quit' to exit>");
            Scanner sc = new Scanner(System.in);
            String start = sc.next();// 起始城市
            if (start.equals("quit")) {
                System.out.println("Service is over");
                sc.close();// 会关闭System.in
                return;
            }
            String destination = sc.next();// 目标城市
            if (start.equals(destination)) {
                System.out.println("The starting city and the ending city can not be the same\nPlease retry");
                continue;
            }

            Pair<Integer, Integer> res = rs.calc_route(start, destination);

            if (res.getKey() == Integer.MAX_VALUE)
                System.out.println("The route from " + start + " to " + destination + " does not exist");
            else {
                System.out.println("The cheapest route from " + start + " to " + destination + " costs " + res.getKey() + " euros and spans " + res.getValue());// 起点、终点、开销、里程
                System.out.println(rs.recover_route(destination));// 具体路径
            }
        }
    }
}