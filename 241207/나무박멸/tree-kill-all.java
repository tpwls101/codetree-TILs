import java.io.*;
import java.util.*;

/**
<주의사항1>
런타임 에러!
더 이상 제초할 나무가 없는 경우를 생각해줘야 한다.
candidate.size()가 0인 경우 처리해주기!

<주의사항2>
문제를 제대로 구현하지 못함
제초할 때 나무가 없는 곳이나 벽을 만나도 제초제를 뿌려줘야 한다.
이를 check() 함수에서 처리하면 제초제 뿌릴 위치를 확인할 때 뿌려지는 곳이 생기니
position 리스트에 담아서 한꺼번에 처리할 것!!
*/

public class Main {

    static int N; // 격자의 크기
    static int M; // 박멸이 진행되는 년 수
    static int K; // 제초제의 확산 범위
    static int C; // 제초제가 남아있는 년 수
    static int[][] arr;
    static int[][] original;
    static int[][] remove; // 제초제가 남아있는 년 수 저장
    static List<Node> candidate; // 제초제를 뿌릴 위치 후보
    static List<Node> position; // 박멸할 나무 위치
    static int answer = 0; // M년 동안 박멸한 나무의 총 합

    static int[] dx = { 0, 1, 0, -1 };
    static int[] dy = { 1, 0, -1, 0 };

    static class Node implements Comparable<Node> {
        int x;
        int y;
        int removeCnt; // 제초로 박멸되는 나무의 수

        Node(int x, int y, int removeCnt) {
            this.x = x;
            this.y = y;
            this.removeCnt = removeCnt;
        }

        @Override
        public int compareTo(Node o) {
            if(this.removeCnt == o.removeCnt) {
                if(this.x == o.x) {
                    return this.y - o.y; // 3. 열 기준 오름차순
                }
                return this.x - o.x; // 2. 행 기준 오름차순
            }
            return o.removeCnt - this.removeCnt; // 1. 박멸할 나무 수 기준 내림차순
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        arr = new int[N][N];
        original = new int[N][N];
        remove = new int[N][N];

        for(int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<N; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // M년간 반복
        for(int i=0; i<M; i++) {
            // 1. 인접한 나무의 수만큼 성장(동시에)
            grow();

            // 2. 번식(동시에)
            spread();
            
            // 3. 제초제 뿌리기
            removeTree();
        }

        System.out.println(answer);
    }

    static void grow() {
        for(int i=0; i<N; i++) {
            for(int j=0; j<N; j++) {
                if(arr[i][j] > 0) {
                    int cnt = 0; // 인접한 칸 중 나무가 있는 칸의 수
                    for(int k=0; k<4; k++) {
                        int nx = i + dx[k];
                        int ny = j + dy[k];

                        if(nx >= 0 && nx < N && ny >= 0 && ny < N) {
                            if(arr[nx][ny] > 0) {
                                cnt++;
                            }
                        }
                    }
                    arr[i][j] += cnt;
                }
            }
        }
    }

    static void spread() {
        // 배열 복사
        for(int i=0; i<N; i++) {
            original[i] = arr[i].clone();
        }

        for(int i=0; i<N; i++) {
            for(int j=0; j<N; j++) {
                if(original[i][j] > 0) {
                    List<Node> list = new ArrayList<>();
                    int cnt = 0; // 인접한 칸 중 빈 칸의 수(벽, 나무, 제초제 모두x)
                    
                    for(int k=0; k<4; k++) {
                        int nx = i + dx[k];
                        int ny = j + dy[k];

                        if(nx >= 0 && nx < N && ny >= 0 && ny < N) {
                            // 빈 칸이고 제초제도 없으면 번식 가능한 곳
                            if(original[nx][ny] == 0 && remove[nx][ny] == 0) {
                                list.add(new Node(nx, ny, 0));
                                cnt++;
                            }
                        }
                    }

                    for(Node node : list) {
                        arr[node.x][node.y] += original[i][j] / cnt;
                    }
                }
            }
        }
    }

    static void removeTree() {
        candidate = new ArrayList<>();

        for(int i=0; i<N; i++) {
            for(int j=0; j<N; j++) {
                if(arr[i][j] > 0) { // 나무가 있는 곳만 확인
                    int removedCnt = check(i, j); // 각 칸에서 박멸할 나무의 수
                    candidate.add(new Node(i, j, removedCnt));
                }
            }
        }

        Collections.sort(candidate);
        if(candidate.size() < 1) return; // 더 이상 제초할 나무가 없는 경우 처리 필수!!
        Node best = candidate.get(0); // 제초제 뿌릴 위치 선정 완료

        // 제초제 뿌리기 전에 1년 지났으니 제초제의 남은 년 수 갱신
        for(int i=0; i<N; i++) {
            for(int j=0; j<N; j++) {
                if(remove[i][j] > 0) {
                    remove[i][j] -= 1;
                }
            }
        }

        int removedCnt = check(best.x, best.y); // 박멸할 나무 위치 리스트(position) 얻을 수 있음
        for(Node node : position) {
            // 나무가 있는 곳만 박멸시키고, 벽이나 없는 곳은 그대로 유지
            if(arr[node.x][node.y] > 0) {
                arr[node.x][node.y] = 0;
            }
            remove[node.x][node.y] = C; // 박멸한 위치에 제초제가 남아있을 년 수 저장
        }
        answer += removedCnt;
    }

    static int check(int x, int y) {
        position = new ArrayList<>();
        position.add(new Node(x, y, 0));

        int sum = 0; // (x,y) 기준 대각선으로 박멸할 수 있는 나무 수

        // 제초제를 뿌리다가 벽이 있거나 나무가 없는 칸을 만나면 전파 stop
        for(int i=1; i<=K; i++) {
            if(isRange(x-i, y+i)) { // 우상
                position.add(new Node(x-i, y+i, 0));
                if(arr[x-i][y+i] <= 0) break;
                sum += arr[x-i][y+i];
            }
        }
        for(int i=1; i<=K; i++) {
            if(isRange(x+i, y+i)) { // 우하
                position.add(new Node(x+i, y+i, 0));
                if(arr[x+i][y+i] <= 0) break;
                sum += arr[x+i][y+i];
            }
        }
        for(int i=1; i<=K; i++) {
            if(isRange(x+i, y-i)) { // 좌하
                position.add(new Node(x+i, y-i, 0));
                if(arr[x+i][y-i] <= 0) break;
                sum += arr[x+i][y-i];
            }
        }
        for(int i=1; i<=K; i++) {
            if(isRange(x-i, y-i)) { // 좌상
                position.add(new Node(x-i, y-i, 0));
                if(arr[x-i][y-i] <= 0) break;
                sum += arr[x-i][y-i];
            }
        }
        sum += arr[x][y]; // (x,y) 값 더해주기
        return sum;
    }

    static boolean isRange(int x, int y) {
        if(x >= 0 && x < N && y >= 0 && y < N) {
            return true;
        }
        return false;
    }
}