package controller;

import java.io.*;
import java.util.*;

public class LeaderboardManager {
    public static final LeaderboardManager INSTANCE = new LeaderboardManager();
    private static final String FILE = "leaderboard.txt";
    private static final int MAX = 10;
    private List<Entry> list = new ArrayList<>();

    private LeaderboardManager() {
        load();
    }

    public static class Entry implements Comparable<Entry> {
        public String name;
        public int score;
        public int lvl;
        public long time;

        public Entry(String name, int score, int lvl) {
            this.name = name;
            this.score = score;
            this.lvl = lvl;
            this.time = System.currentTimeMillis();
        }

        public Entry(String name, int score, int lvl, long time) {
            this.name = name;
            this.score = score;
            this.lvl = lvl;
            this.time = time;
        }

        @Override
        public int compareTo(Entry o) {
            int c = Integer.compare(o.score, this.score);
            if (c != 0) return c;
            c = Integer.compare(o.lvl, this.lvl);
            if (c != 0) return c;
            return Long.compare(this.time, o.time);
        }
    }

    public boolean isTop(int score) {
        if (list.size() < MAX) return true;
        return score > list.get(list.size() - 1).score;
    }

    public void add(String name, int score, int lvl) {
        list.add(new Entry(name, score, lvl));
        Collections.sort(list);
        if (list.size() > MAX) {
            list = list.subList(0, MAX);
        }
        save();
    }

    public List<Entry> getList() {
        return new ArrayList<>(list);
    }

    public int getRank(int score) {
        for (int i = 0; i < list.size(); i++) {
            if (score > list.get(i).score) return i + 1;
        }
        return list.size() + 1;
    }

    private void save() {
        try (PrintWriter w = new PrintWriter(new FileWriter(FILE))) {
            for (Entry e : list) {
                w.println(e.name + "|" + e.score + "|" + e.lvl + "|" + e.time);
            }
        } catch (IOException e) {
        }
    }

    private void load() {
        File f = new File(FILE);
        if (!f.exists()) {
            list.clear();
            return;
        }
        try (BufferedReader r = new BufferedReader(new FileReader(FILE))) {
            list.clear();
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length == 4) {
                    list.add(new Entry(p[0], Integer.parseInt(p[1]), 
                                      Integer.parseInt(p[2]), Long.parseLong(p[3])));
                }
            }
            Collections.sort(list);
        } catch (IOException | NumberFormatException e) {
            list.clear();
        }
    }

    public void clear() {
        list.clear();
        save();
    }
}
