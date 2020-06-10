int main() {
    int ranking = 1;
    int score = 0;
    int float_test = 3.4;

    if (ranking == 1){
        score = 10;
        grade = "A";
    }else{
        if(ranking == 2){
            score = 8;
            grade = "B";
        }else{
            if(ranking == 3){
                score = "5";
                grade = "C";
            }else{
                score = 0;
                grade = "D";
            }
        }
    }

    return 0;
}

char test_function(int a, int b) {
    int result = 0;
    int i;

    if (a < b) {
	for(i=0; i<b; i=i+1){
            result = result + i;
        }
    }

    if (a == b){
        while(b != 0){
            result = result * a;
            b = b-1;
        }
    }

    if(a > b){
        result = a / b - 1;
    }

    return result;
}
