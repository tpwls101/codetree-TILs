import java.io.*;
import java.util.*;

public class Main {

    static int N; // 격자의 크기
    static int L;
    static int R;
    static int[][] arr;
    static int[][] copy;
    static boolean[][] visited;
    static boolean flag = true; // 계란 이동이 있는지
    static int answer = 0; // 계란의 이동이 일어나는 횟수

    static int[] dx = { 0, 1, 0, -1 };
    static int[] dy = { 1, 0, -1, 0 };

    static class Node {
        int x;
        int y;
        
        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        L = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken());
        
        arr = new int[N][N];
        copy = new int[N][N];
        visited = new boolean[N][N];

        for(int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<N; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for(int i=0; i<N; i++) {
            copy[i] = arr[i].clone();
        }

        while(flag) {
            flag = false;

            for(int i=0; i<N; i++) {
                for(int j=0; j<N; j++) {
                    if(!visited[i][j]) {
                        bfs(new Node(i, j));
                    }
                }
            }

            for(int i=0; i<N; i++) {
                arr[i] = copy[i].clone();
            }

            if(flag) answer++;
            else break;
        }

        System.out.println(answer);
    }

    static void bfs(Node node) {
        List<Node> list = new ArrayList<>(); // 값이 변할 곳의 위치를 저장

        Queue<Node> queue = new ArrayDeque<>();
        queue.add(node);
        visited[node.x][node.y] = true;
        list.add(node);
        
        int cnt = 1;
        int sum = arr[node.x][node.y];

        while(!queue.isEmpty()) {
            Node current = queue.poll();

            for(int i=0; i<4; i++) {
                int nx = current.x + dx[i];
                int ny = current.y + dy[i];

                if(nx >= 0 && nx < N && ny >= 0 && ny < N) {
                    int diff = Math.abs(arr[current.x][current.y] - arr[nx][ny]);
                    if(!visited[nx][ny] && diff >= L && diff <= R) {
                        queue.add(new Node(nx, ny));
                        visited[nx][ny] = true;
                        cnt++;
                        sum += arr[nx][ny];
                        list.add(new Node(nx, ny));
                    }
                }
            }
        }

        // 계란틀의 선이 분리된 곳이 있으면 합친 후 다시 분리하기
        if(list.size() >= 2) {
            flag = true;
            int value = sum / cnt;
            for(Node n : list) {
                copy[n.x][n.y] = value;
            }
        }
    }
}