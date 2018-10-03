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

//�˰����� ����� �����ϱ� ���� Ŭ����
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

//�����߽� ���ϴ� �Լ�
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

// ODsay���� �ð��� �޾ƿ��� �����̹Ƿ�, ���⼭ �ɸ��� �ð��� �Է¹ްԲ� �ϸ� �� �� ����.
// ������ �ð��� ���߿� �Ÿ��� �ٲٸ� �� ��.

// �Ÿ� ����.
/*
// 3. �����߽��� �̿��Ͽ� �� ��ǥ���� ������ ��Į�� ���ϱ�. �� �� ������ �Ÿ� = ��Ʈ { (x1 - x2)^2 + (y1 - y2)^2 }
// 4. ��Į���� ũ���� ����ġ �ű��
double *calcWeight(User *user, User centerUser, int num) {
	//�ο� �� ��ŭ ����ġ �迭 �����Ҵ�
	double *weight = (double*)malloc(num * sizeof(double));
	// �߽��������� �� ����ڱ����� �Ÿ� ���� �迭 �����Ҵ�
	double *distance = (double*)malloc(num * sizeof(double));

	for (int i = 0; i < num; i++) {
		weight[i] = 0.1; //����ġ�� 1�� �ʱ�ȭ
		//�� ��ǥ���� �߽���ǥ������ �Ÿ��� ����
		distance[i] = sqrt( powl(user[i].getX() - centerUser.getX() , 2.0) + powl(user[i].getY() - centerUser.getY(), 2.0) );
	}

	// ������ ���� �Ÿ��� ���� ����ġ �ű�� (�Ÿ��� �� ���� ���� ����ġ ����)
	for (int i = 0; i < num; i++) {
		for (int j = 0; j < num; j++) {
			// i��° �Ÿ��� j��° �Ÿ����� ũ�� ����ġ ����.
			if (distance[i] > distance[j])
				weight[i] += 0.1;
		}
	}
	
	return weight;
}
*/

// �ð� ����. (���� ����ϴ� ����̶� ����ϰ� �ϱ�����, �ð��� �Է¹޾� �׿����� ����ġ�� �ű�)
double *calcWeight(User *user, User centerUser, int num) {
	
	//�ο� �� ��ŭ ����ġ �迭 �����Ҵ�
	double *weight = (double*)malloc(num * sizeof(double));

	// �߽��������� �� ����ڱ����� �ɸ��� �ð� �ӽ� ����
	double time = 0.0;

	for (int i = 0; i < num; i++) {
		weight[i] = 0.1; //����ġ�� �ʱⰪ���� �ʱ�ȭ
						 //�� ��ǥ���� �߽���ǥ������ �ҿ� �ð��� ����
		cout << i + 1 << "��° ����ڰ� �߽���ǥ���� ���µ� �ɸ��� �ð� : ";
		cin >> time;
		user[i].setTime(time); // �� ������ �ɸ��� �ð��� �ǹ��ϴ� ������ ����
	}

	// ������ ���� �Ÿ��� ���� ����ġ �ű�� (�Ÿ��� �� ���� ���� ����ġ ����)
	for (int i = 0; i < num; i++) {
		for (int j = 0; j < num; j++) {
			// i��° �ɸ��� �ð��� j��° �ɸ��� �ð����� ũ�� ����ġ ����.
			if (user[i].getTime() > user[j].getTime())
				weight[i] += 0.1;
		}
	}

	return weight;
}


// ����ġ �׽�Ʈ �ڵ� ���� 
/*
	�ɸ��� �ð��� �Ѱ���ߴ�.
*/

// 5. �� ���Ϳ� ����ġ��ŭ ���� ���� ���ϰ�, �� ���Ͱ��� ��ǥ�� ��Ÿ����

VectorXY getVector(double *weight, User *user, User centerUser, int num) {
	/*
	�߽�xy��ǥ - ����1�� xy��ǥ -> ����
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

// ����ġ �׽�Ʈ �ڵ� ����
VectorXY getVector(User *user, int num) {
	/*
	�߽�xy��ǥ - ����1�� xy��ǥ -> ����
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
		// �ӽ� x, y��ǥ = z (�ð��� ��� *  x or y ��ǥ���� / �ɸ��� �ð�)
		vectorRX += ((timeAvg) * ( user[i].getX() ) / user[i].getTime());
		vectorRY += ((timeAvg) * (user[i].getY() ) / user[i].getTime());
	}

	// ��Ȯ�� x, y��ǥ = z (�ð��� ��� *  x or y ��ǥ���� / �ɸ��� �ð�) / count
	vector.setXY((vectorRX/num), (vectorRY/num));

	return vector;
}
// ����ġ �׽�Ʈ �ڵ� ��

int main() {

	/*
	0. ����� �� �Է¹ޱ�
	1. �� ������� ��ǥ(x,y)�� �Է¹ޱ�.
	2. �����߽� ���ϱ�
	3. �����߽��� �̿��Ͽ� �� ��ǥ���� ������ ��Į�� ���ϱ�. �� �� ������ �Ÿ� = ��Ʈ { (x1 - x2)^2 + (y1 - y2)^2 }
	4. ��Į���� ũ���� ����ġ �ű��.
	5. �� ���Ϳ� ����ġ��ŭ ���� ���� ���ϰ�, �� ���Ͱ��� ��ǥ�� ��Ÿ����
	*/

	int num = 0, choose = 0;
	User *user;
	double *weight;

	while (1) {
		system("cls");
		// 0. ����� �� �Է� �ޱ�
		cout << "�� �� : ";
		cin >> num;

		user = (User*)malloc(num * sizeof(User));

		// 1. ����� ����ŭ ��ǥ �Է� �ޱ�
		for (int i = 0; i < num; i++) {
			double x = 0, y = 0;
			cout << i + 1 << "��°  ������� x ��ǥ �Է� : ";
			cin >> x;
			cout << i + 1 << "��°  ������� y ��ǥ �Է� : ";
			cin >> y;

			user[i].setXY(x, y);
		}

		// 2. �����߽� ���ϱ�
		User centerUser = getCenter(user, num);

		cout << endl << "�����߽��� ��ǥ" << endl << "x : " << centerUser.getX() << "  y : " << centerUser.getY() << endl << endl;

		VectorXY vectorA, vectorB;

		// 3,4,5
		//---------------------------------------------------------------------------------------------------
		// �츮�� ������ �ð� ����ġ �˰���
		// ����ġ �迭 �����ϱ�
		weight = calcWeight(user, centerUser, num);

		// ������ ����ġ �迭�� �Ѱ�, ���� ���� �ϱ�.
		vectorA = getVector(weight, user, centerUser, num);


		//---------------------------------------------------------------------------------------------------
		// ���簡 ������ ����ġ �˰���(�׳� ���� ���ֱ淡 �ݿ����Ѻ�)
		// �����߽��� ����� �ʿ� ����, �� ����ڵ��� ��ǥ���� ������ �̿� ����
		// �� �˰����� �������� ��, ������ ���� �츮�� ������ �˰����� ����� ��� �ٸ��� �˾� �� �ʿ䰡 ����

		// �� ����ڰ� �ɸ��� �ð��� �Ѱ� ��ǥ���� �ϱ�
		vectorB = getVector(user, num);

		cout << endl << "�ð� ����ġ �˰����� �̿��Ͽ� ���� ����� ��ǥ" << endl << "x : " << vectorA.getX() << "  y : " << vectorA.getY() << endl;
		cout << "��Ÿ �˰����� �̿��Ͽ� ���� ����� ��ǥ" << endl << "x : " << vectorB.getX() << "  y : " << vectorB.getY() << endl;

		free(user);

		system("pause");
	}
	return 0;
}