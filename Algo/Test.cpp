#include <iostream>
#include <malloc.h>
#include <math.h>
using namespace std;

class User {
	double x, y;
	double time;
public : 
	User() {
		x = 0.0;
		y = 0.0;
		time = 0.0;
	}

	void setXY(double uX, double uY) {
		x = uX;
		y = uY;
	}

	void setTime(double t) {
		time = t;
	}

	double getX() {
		return x;
	}

	double getY() {
		return y;
	}

	double getTime() {
		return time;
	}
};

//알고리즘의 결과를 저장하기 위한 클래스
class VectorXY {
	private : 
		double vX, vY;
	public : 
		void setXY(double x, double y) {
			vX = x;
			vY = y;
		}

		double getX() {
			return vX;
		}

		double getY() {
			return vY;
		}

};

//무게중심 구하는 함수
User getCenter(User *user, int count) {
	double centerX = 0;
	double centerY = 0;
	double sumX = 0;
	double sumY = 0;
	User centerUser;

	for (int i = 0; i < count; i++) {
		sumX += user[i].getX();
		sumY += user[i].getY();
	}

	centerX = sumX / count;
	centerY = sumY / count;

	centerUser.setXY(centerX, centerY);

	return centerUser;
}

// ODsay에서 시간을 받아오는 형식이므로, 여기서 걸리는 시간을 입력받게끔 하면 될 것 같음.
// 여기의 시간을 나중에 거리로 바꾸면 될 것.

// 거리 기준.
/*
// 3. 무게중심을 이용하여 각 좌표와의 벡터의 스칼라값 구하기. 두 점 사이의 거리 = 루트 { (x1 - x2)^2 + (y1 - y2)^2 }
// 4. 스칼라값의 크기대로 가중치 매기기
double *calcWeight(User *user, User centerUser, int num) {
	//인원 수 만큼 가중치 배열 동적할당
	double *weight = (double*)malloc(num * sizeof(double));
	// 중심지점에서 각 사용자까지의 거리 저장 배열 동적할당
	double *distance = (double*)malloc(num * sizeof(double));

	for (int i = 0; i < num; i++) {
		weight[i] = 0.1; //가중치를 1로 초기화
		//각 좌표에서 중심좌표까지의 거리를 구함
		distance[i] = sqrt( powl(user[i].getX() - centerUser.getX() , 2.0) + powl(user[i].getY() - centerUser.getY(), 2.0) );
	}

	// 위에서 나온 거리를 토대로 가중치 매기기 (거리가 멀 수록 높은 가중치 받음)
	for (int i = 0; i < num; i++) {
		for (int j = 0; j < num; j++) {
			// i번째 거리가 j번째 거리보다 크면 가중치 증가.
			if (distance[i] > distance[j])
				weight[i] += 0.1;
		}
	}
	
	return weight;
}
*/

// 시간 기준. (실제 사용하는 방법이랑 비슷하게 하기위해, 시간을 입력받아 그에따른 가중치를 매김)
double *calcWeight(User *user, User centerUser, int num) {
	
	//인원 수 만큼 가중치 배열 동적할당
	double *weight = (double*)malloc(num * sizeof(double));

	// 중심지점에서 각 사용자까지의 걸리는 시간 임시 저장
	double time = 0.0;

	for (int i = 0; i < num; i++) {
		weight[i] = 0.1; //가중치를 초기값으로 초기화
						 //각 좌표에서 중심좌표까지의 소요 시간을 구함
		cout << i + 1 << "번째 사용자가 중심좌표까지 오는데 걸리는 시간 : ";
		cin >> time;
		user[i].setTime(time); // 각 유저의 걸리는 시간을 의미하는 변수에 저장
	}

	// 위에서 나온 거리를 토대로 가중치 매기기 (거리가 멀 수록 높은 가중치 받음)
	for (int i = 0; i < num; i++) {
		for (int j = 0; j < num; j++) {
			// i번째 걸리는 시간이 j번째 걸리는 시간보다 크면 가중치 증가.
			if (user[i].getTime() > user[j].getTime())
				weight[i] += 0.1;
		}
	}

	return weight;
}


// 가중치 테스트 코드 시작 
/*
	걸리는 시간을 넘겨줘야댐.
*/

// 5. 각 벡터에 가중치만큼 곱한 값을 구하고, 그 벡터값의 좌표를 나타내기

VectorXY getVector(double *weight, User *user, User centerUser, int num) {
	/*
	중심xy좌표 - 유저1의 xy좌표 -> 저장
	*/
	VectorXY vector;
	double vectorRX = 0;
	double vectorRY = 0;

	for (int i = 0; i < num; i++) {
		vectorRX += (weight[i] * ( user[i].getX() - centerUser.getX() ) );
		vectorRY += (weight[i] * (user[i].getY() - centerUser.getY() ) );
	}
	vector.setXY(vectorRX, vectorRY);

	return vector;
}

// 가중치 테스트 코드 시작
VectorXY getVector(User *user, int num) {
	/*
	중심xy좌표 - 유저1의 xy좌표 -> 저장
	*/
	VectorXY vector;
	double vectorRX = 0.0;
	double vectorRY = 0.0;
	double timeSum = 0.0;
	double timeAvg = 0.0;

	for (int i = 0; i < num; i++) {
		timeSum += user[i].getTime();
	}

	timeAvg = (double) (timeSum / num);
	
	for (int i = 0; i < num; i++) {
		// 임시 x, y좌표 = z (시간의 평균 *  x or y 좌표차이 / 걸리는 시간)
		vectorRX += ((timeAvg) * ( user[i].getX() ) / user[i].getTime());
		vectorRY += ((timeAvg) * (user[i].getY() ) / user[i].getTime());
	}

	// 정확한 x, y좌표 = z (시간의 평균 *  x or y 좌표차이 / 걸리는 시간) / count
	vector.setXY((vectorRX/num), (vectorRY/num));

	return vector;
}
// 가중치 테스트 코드 끝

int main() {

	/*
	0. 사용자 수 입력받기
	1. 각 사용자의 좌표(x,y)를 입력받기.
	2. 무게중심 구하기
	3. 무게중심을 이용하여 각 좌표와의 벡터의 스칼라값 구하기. 두 점 사이의 거리 = 루트 { (x1 - x2)^2 + (y1 - y2)^2 }
	4. 스칼라값의 크기대로 가중치 매기기.
	5. 각 벡터에 가중치만큼 곱한 값을 구하고, 그 벡터값의 좌표를 나타내기
	*/

	int num = 0, choose = 0;
	User *user;
	double *weight;

	while (1) {
		system("cls");
		// 0. 사용자 수 입력 받기
		cout << "몇 명 : ";
		cin >> num;

		user = (User*)malloc(num * sizeof(User));

		// 1. 사용자 수만큼 좌표 입력 받기
		for (int i = 0; i < num; i++) {
			double x = 0, y = 0;
			cout << i + 1 << "번째  사용자의 x 좌표 입력 : ";
			cin >> x;
			cout << i + 1 << "번째  사용자의 y 좌표 입력 : ";
			cin >> y;

			user[i].setXY(x, y);
		}

		// 2. 무게중심 구하기
		User centerUser = getCenter(user, num);

		cout << endl << "무게중심의 좌표" << endl << "x : " << centerUser.getX() << "  y : " << centerUser.getY() << endl << endl;

		VectorXY vectorA, vectorB;

		// 3,4,5
		//---------------------------------------------------------------------------------------------------
		// 우리가 생각한 시간 가중치 알고리즘
		// 가중치 배열 저장하기
		weight = calcWeight(user, centerUser, num);

		// 저장한 가중치 배열을 넘겨, 벡터 연산 하기.
		vectorA = getVector(weight, user, centerUser, num);


		//---------------------------------------------------------------------------------------------------
		// 희재가 생각한 가중치 알고리즘(그냥 생각 해주길래 반영시켜봄)
		// 무게중심을 고려할 필요 없이, 각 사용자들의 좌표값만 있으면 이용 가능
		// 이 알고리즘을 적용했을 때, 찍히는 점이 우리가 생각한 알고리즘의 결과와 어떻게 다른지 알아 볼 필요가 있음

		// 각 사용자가 걸리는 시간을 넘겨 좌표연산 하기
		vectorB = getVector(user, num);

		cout << endl << "시간 가중치 알고리즘을 이용하여 구한 결과의 좌표" << endl << "x : " << vectorA.getX() << "  y : " << vectorA.getY() << endl;
		cout << "기타 알고리즘을 이용하여 구한 결과의 좌표" << endl << "x : " << vectorB.getX() << "  y : " << vectorB.getY() << endl;

		free(user);

		system("pause");
	}
	return 0;
}