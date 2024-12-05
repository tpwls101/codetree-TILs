import java.io.*;
import java.util.*;

public class Main {

    static int N; // 일의 양
    static int[][] arr; // 업무 간의 상성
    static boolean[] visited;
    static int[] morning; // 아침에 하는 일
    static int[] evening; // 저녁에 하는 일
    static int answer = Integer.MAX_VALUE; // 아침과 저녁의 업무 강도의 차이의 최솟값

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());

        arr = new int[N][N];
        visited = new boolean[N];
        morning = new int[N/2];
        evening = new int[N/2];

        for(int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<N; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 1. 조합 구하기
        comb(0, 0);

        System.out.println(answer);
    }

    static void comb(int start, int cnt) {
        if(cnt == N/2) {
            // 아침에 하는 일이 정해지면 저녁에 하는 일도 정해짐
            decideEvening(morning);
            // 2. 업무 강도 구하기
            int morningIntensity = getWorkIntensity(morning);
            int eveningIntensity = getWorkIntensity(evening);
            // 3. 업무 강도의 차이를 최소로 갱신
            answer = Math.min(answer, Math.abs(morningIntensity - eveningIntensity));
            return;
        }

        for(int i=start; i<N; i++) {
            if(visited[i]) continue;
            morning[cnt] = i;
            visited[i] = true;
            comb(i+1, cnt+1);
            visited[i] = false;
        }
    }

    static void decideEvening(int[] morning) {
        List<Integer> list = new ArrayList<>();
        int index = 0;

        for(int i=0; i<N; i++) {
            if(index < N/2 && morning[index] == i) {
                index++;
                continue;
            } else {
                list.add(i);
            }
        }

        for(int i=0; i<evening.length; i++) {
            evening[i] = list.get(i);
        }
    }

    static int getWorkIntensity(int[] array) {
        int intensity = 0;
        for(int i=0; i<array.length; i++) {
            for(int j=i+1; j<array.length; j++) {
                int from = array[i];
                int to = array[j];
                intensity += arr[from][to];
                intensity += arr[to][from];
            }
        }
        return intensity;
    }
}