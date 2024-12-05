import java.io.*;
import java.util.*;

public class Main {

    static int N;
    static int M;
    static int[][] arr;
    static boolean[][] visited;
    static Queue<Node> queue = new ArrayDeque<>();
    static int answer = 1; // 자동차가 거쳐간 도로의 총 면적

    // 상우하좌
    static int[] dx = { -1, 0, 1, 0 };
    static int[] dy = { 0, 1, 0, -1 };

    static class Node {
        int x;
        int y;
        int dir;

        Node(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        arr = new int[N][M];
        visited = new boolean[N][M];

        st = new StringTokenizer(br.readLine());
        int start_x = Integer.parseInt(st.nextToken());
        int start_y = Integer.parseInt(st.nextToken());
        int direction = Integer.parseInt(st.nextToken());
        queue.add(new Node(start_x, start_y, direction));
        visited[start_x][start_y] = true;

        for(int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<M; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        while(!queue.isEmpty()) {
            Node current = queue.poll();

            boolean flag = false; // 네 방향 중 하나라도 갈 수 있는가

            for(int i=0; i<4; i++) {
                int nd = (current.dir - 1 + 4) % 4; // 현재 방향 기준 왼쪽
                int nx = current.x + dx[nd];
                int ny = current.y + dy[nd];

                // 인도거나 이미 방문한 도로면 다시 좌회전
                if(arr[nx][ny] == 1 || visited[nx][ny]) {
                    current.dir = nd;
                    continue;
                } else { // 다음 칸이 도로고 방문하지도 않았으면 전진 가능
                    queue.add(new Node(nx, ny, nd));
                    visited[nx][ny] = true;
                    flag = true;
                    answer++;
                    break;
                }
            }
            
            // 네 방향 모두 갈 수 없으면 방향 유지한 채로 한 칸 후진
            if(!flag) {
                int nd = (current.dir + 2) % 4; // 현재와 반대 방향
                int nx = current.x + dx[nd];
                int ny = current.y + dy[nd];

                // 뒷 공간이 인도여서 후진도 불가능하면 stop
                if(arr[nx][ny] == 1) break;

                queue.add(new Node(nx, ny, current.dir)); // 현재 방향은 유지해야 함
            }
        }

        System.out.println(answer);
    }
}