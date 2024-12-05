import java.io.*;
import java.util.*;

/**
1. 모든 병원 중 M개의 병원 고르기 (조합)
2. 고른 M개의 병원에 대해 각 사람마다 병원 거리 구하기
    - M개의 병원에 대해 모두 거리를 구해보고 가장 작은 값이 병원 거리다.
3. 병원 거리의 합을 구해 모든 경우의 수마다 최소로 갱신
*/

public class Main {

    static int N; // NxN 크기의 도시
    static int M; // 남는 병원의 개수
    static int[][] arr; // 0(빈 칸), 1(사람), 2(병원)
    static List<Node> hospital; // 모든 병원의 위치를 저장하는 리스트
    static List<Node> person; // 모든 사람의 위치를 저장하는 리스트 
    static boolean[] visited;
    static Node[] selected; // 선택된 병원
    static int answer = Integer.MAX_VALUE; // 병원 거리의 총 합의 최솟값

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
        hospital = new ArrayList<>();
        person = new ArrayList<>();
        selected = new Node[M];

        for(int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<N; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());

                // 병원의 위치 저장
                if(arr[i][j] == 2) hospital.add(new Node(i, j));
                // 사람의 위치 저장
                if(arr[i][j] == 1) person.add(new Node(i, j));
            }
        }

        visited = new boolean[hospital.size()];

        // M개의 병원 고르기
        comb(0, 0);
        
        System.out.println(answer);
    }

    static void comb(int start, int cnt) {
        if(cnt == M) {
            // 병원 거리의 총 합 구하기
            int sum = getTotalHospitalDistance();
            // 병원 거리의 총 합을 최소로 갱신
            answer = Math.min(answer, sum);
            return;
        }

        for(int i=start; i<hospital.size(); i++) {
            if(visited[i]) continue;
            selected[cnt] = hospital.get(i);
            visited[i] = true;
            comb(i+1, cnt+1);
            visited[i] = false;
        }
    }

    static int getTotalHospitalDistance() {
        int sum = 0; // 병원 거리의 총 합
        for(int i=0; i<person.size(); i++) {
            int hospitalDistance = Integer.MAX_VALUE; // 한 사람의 병원 거리

            for(int j=0; j<selected.length; j++) {
                int temp = Math.abs(person.get(i).x - selected[j].x) + Math.abs(person.get(i).y - selected[j].y);
                hospitalDistance = Math.min(hospitalDistance, temp);
            }

            sum += hospitalDistance;
        }
        return sum;
    }
}