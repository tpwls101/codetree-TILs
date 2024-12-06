import java.io.*;
import java.util.*;

/**
<특수 영양제를 이동시킬 때 주의사항>
이중 for문 돌리면서 visited[i][j]가 참인 것을 이동시키면
이미 전에 이동시켜 참이된 곳을 다시 이동시키게 된다.
따라서 리스트에 이동시킬 위치를 담아두고 해당 위치만 이동시켜야 한다.

+ visited 배열도 매년 초기화해야 한다!
*/

public class Main {

    static int N; // 격자의 크기
    static int M; // 리브로수를 키우는 총 년 수
    static int[][] arr; // 리브로수의 높이를 저장
    static boolean[][] visited; // 특수 영양제의 위치를 저장하기 위해 사용
    static List<Node> list = new ArrayList<>(); // 이동시킬 특수 영양제의 위치를 저장

    static int[] dx = { 0, -1, -1, -1, 0, 1, 1, 1 };
    static int[] dy = { 1, 1, 0, -1, -1, -1, 0, 1 };

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
        M = Integer.parseInt(st.nextToken());

        arr = new int[N][N];
        visited = new boolean[N][N];

        for(int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<N; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        list.add(new Node(N-2, 0));
        list.add(new Node(N-2, 1));
        list.add(new Node(N-1, 0));
        list.add(new Node(N-1, 1));

        // M번의 명령 수행
        for(int i=0; i<M; i++) {
            st = new StringTokenizer(br.readLine());
            int d = Integer.parseInt(st.nextToken()) - 1; // 방향
            int p = Integer.parseInt(st.nextToken()); // 이동 칸 수

            visited = new boolean[N][N]; // 초기화 필수

            // 주어진 방향으로 이동
            move(d, p);
            
            // 특수 영양제를 투입한 리브로수는 대각선으로 인접한 방향에 높이가 1 이상인 리브로수가 있는 수만큼 더 성장
            moreGrow();
            
            // 해당 년도에 특수 영양제를 맞은 땅을 제외하고 높이가 2 이상인 리브로수를 2만큼 잘라낸다
            cut();
        }

        // M년이 지난 뒤 남아있는 리브로수 높이의 총 합 구하기
        int sum = 0;
        for(int i=0; i<N; i++) {
            for(int j=0; j<N; j++) {
                sum += arr[i][j];
            }
        }
        System.out.println(sum);
    }

    // 특수 영양제를 이동시키는 메서드
    static void move(int d, int p) {
        for(Node node : list) {
            int nx = (node.x + dx[d]*p + N) % N;
            int ny = (node.y + dy[d]*p + N) % N;
            visited[nx][ny] = true;
            arr[nx][ny] += 1; // 특수 영양제가 투입되면 높이 1 증가
        }
    }

    static void moreGrow() {
        // 원본 배열 복사
        int[][] original = new int[N][N];
        for(int i=0; i<N; i++) {
            original[i] = arr[i].clone();
        }

        for(int i=0; i<N; i++) {
            for(int j=0; j<N; j++) {
                if(visited[i][j]) {
                    int cnt = 0; // 대각선으로 높이가 1 이상인 리브로수의 수
                    for(int k=0; k<4; k++) {
                        int nx = i + dx[2*k+1];
                        int ny = j + dy[2*k+1];

                        if(nx >= 0 && nx < N && ny >= 0 && ny < N) {
                            if(original[nx][ny] >= 1) {
                                cnt++;
                            }
                        }
                    }
                    arr[i][j] += cnt;
                }
            }
        }
    }

    static void cut() {
        list = new ArrayList<>(); // 새로운 특수 영양제 위치를 담기 위해 초기화
        for(int i=0; i<N; i++) {
            for(int j=0; j<N; j++) {
                if(visited[i][j]) continue;
                if(arr[i][j] >= 2) { // 높이가 2 이상인 리브로수를
                    arr[i][j] -= 2; // 높이 2만큼 잘라내고
                    list.add(new Node(i, j)); // 해당 땅 위에 특수 영양제를 올려줌
                }
            }
        }
    }
}