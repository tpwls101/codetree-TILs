import java.io.*;
import java.util.*;

public class Main {

    static String[] arr = new String[4]; // 네 개의 팔각 의자에 앉아있는 사람들의 지역을 저장
    static int K; // 회전 횟수
    static int[] dir = new int[4]; // 모든 의자의 회전 방향을 저장
    static int answer = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;

        for(int i=0; i<4; i++) {
            arr[i] = br.readLine();
        }

        K = Integer.parseInt(br.readLine());

        for(int i=0; i<K; i++) {
            st = new StringTokenizer(br.readLine());
            int chairNum = Integer.parseInt(st.nextToken()) - 1;
            int direction = Integer.parseInt(st.nextToken());

            // 모든 의자의 회전 방향 체크
            checkDirection(chairNum, direction);

            // 각 회전 방향에 따라 의자 회전
            rotate();
        }

        if(arr[0].substring(0, 1).equals("1")) answer += 1;
        if(arr[1].substring(0, 1).equals("1")) answer += 2;
        if(arr[2].substring(0, 1).equals("1")) answer += 4;
        if(arr[3].substring(0, 1).equals("1")) answer += 8;

        System.out.println(answer);
    }

    static void checkDirection(int chairNum, int direction) {
        dir[chairNum] = direction;

        // 회전 의자 기준 오른쪽 확인
        for(int i=chairNum+1; i<4; i++) {
            // 같은 지역이면 회전x
            if(arr[i-1].substring(2, 3).equals(arr[i].substring(6, 7))) {
                dir[i] = 0;
            } else { // 지역이 다르면 반대 방향으로 회전
                dir[i] = -dir[i-1];
            }
        }

        // 회전 의자 기준 왼쪽 확인
        for(int i=chairNum-1; i>=0; i--) {
            // 같은 지역이면 회전x
            if(arr[i+1].substring(6, 7).equals(arr[i].substring(2, 3))) {
                dir[i] = 0;
            } else { // 지역이 다르면 반대 방향으로 회전
                dir[i] = -dir[i+1];
            }
        }
    }

    static void rotate() {
        for(int i=0; i<4; i++) {
            if(dir[i] == 1) { // 시계 방향으로 회전
                arr[i] = arr[i].substring(7, 8) + arr[i].substring(0, 7);
            } else if(dir[i] == -1) { // 반시계 방향으로 회전
                arr[i] = arr[i].substring(1, 8) + arr[i].substring(0, 1);
            }
        }
    }
}