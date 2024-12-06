import java.io.*;
import java.util.*;

public class Main {

    static int N; // 격자의 크기
    static int[][] arr;
    static List<Integer> studentList = new ArrayList<>(); // 학생 번호 순서대로 저장
    static Map<Integer, List<Integer>> map = new HashMap<>();
    static List<Node> candidate; // 한 학생이 앉을 수 있는 자리 후보 저장
    static int answer = 0; // 최종 점수

    static int[] dx = { 0, 1, 0, -1 };
    static int[] dy = { 1, 0, -1, 0 };

    static class Node implements Comparable<Node> {
        int x;
        int y;
        int likeCnt;
        int vacantCnt;

        Node(int x, int y, int likeCnt, int vacantCnt) {
            this.x = x;
            this.y = y;
            this.likeCnt = likeCnt;
            this.vacantCnt = vacantCnt;
        }

        @Override
        public int compareTo(Node o) {
            if(this.likeCnt == o.likeCnt) {
                if(this.vacantCnt == o.vacantCnt) {
                    if(this.x == o.x) {
                        return this.y - o.y; // 4. 열 기준 오름차순
                    }
                    return this.x - o.x; // 3. 행 기준 오름차순
                }
                return o.vacantCnt - this.vacantCnt; // 2. 비어있는 칸 수 기준 내림차순
            }
            return o.likeCnt - this.likeCnt; // 1. 좋아하는 친구 기준 내림차순
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());

        arr = new int[N][N];

        for(int i=0; i<N*N; i++) {
            st = new StringTokenizer(br.readLine());
            int studentNum = Integer.parseInt(st.nextToken());
            studentList.add(studentNum);
            int s1 = Integer.parseInt(st.nextToken());
            int s2 = Integer.parseInt(st.nextToken());
            int s3 = Integer.parseInt(st.nextToken());
            int s4 = Integer.parseInt(st.nextToken());
            map.put(studentNum, Arrays.asList(s1, s2, s3, s4));
        }

        for(int i=0; i<N*N; i++) {
            int studentNum = studentList.get(i);
            findSeat(studentNum);
        }

        // 최종 점수 계산
        for(int i=0; i<N; i++) {
            for(int j=0; j<N; j++) {
                int likeCnt = getLikeAndVacant(i, j, arr[i][j])[0];
                if(likeCnt == 1) answer += 1;
                if(likeCnt == 2) answer += 10;
                if(likeCnt == 3) answer += 100;
                if(likeCnt == 4) answer += 1000;
            }
        }

        System.out.println(answer);
    }

    // 한 학생의 가장 적합한 자리를 찾는 메서드
    static void findSeat(int studentNum) {
        candidate = new ArrayList<>();
        for(int i=0; i<N; i++) {
            for(int j=0; j<N; j++) {
                if(arr[i][j] == 0) { // 자리가 비어있으면 확인
                    // 인접한 칸에 좋아하는 학생과 빈 칸이 몇 개인지 확인
                    int[] likeAndVacant = getLikeAndVacant(i, j, studentNum);
                    // 가능한 자리 후보를 리스트에 저장
                    candidate.add(new Node(i, j, likeAndVacant[0], likeAndVacant[1]));
                }
            }
        }

        // 조건에 맞게 후보 리스트 정렬
        Collections.sort(candidate);
        Node best = candidate.get(0); // 가장 적합한 자리
        arr[best.x][best.y] = studentNum;
    }

    // 인접한 칸에 좋아하는 학생이 몇 명 있는지, 빈 칸이 몇 개인지 확인하는 메서드
    static int[] getLikeAndVacant(int x, int y, int studentNum) {
        int likeCnt = 0; // 좋아하는 학생 수
        int vacantCnt = 0; // 빈칸 수

        for(int i=0; i<4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if(nx >= 0 && nx < N && ny >= 0 && ny < N) {
                if(arr[nx][ny] == 0) vacantCnt++;
                for(int likeNum : map.get(studentNum)) {
                    if(arr[nx][ny] == likeNum) {
                        likeCnt++;
                    }
                }
            }
        }
        return new int[]{likeCnt, vacantCnt};
    }
}