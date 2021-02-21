# JAVA Convention

## 1.명명규칙(Naming) 규칙

1. 변수 이름은 숫자, 영어 사용(언더바금지)
2. 변수와 메소드의 파라미터에는 카멜표기법을 사용한다.(첫글자 소문자 내부단어의 첫글자 대문자)
    - `camelCase`
3. 패키지는 소문자로 구성한다.
    - 단어별 구분을 위한 언더스코어(_), 대문자를 섞지 않는다.
4. 클래스, 인터페이스는 명사여야하고 대문자 카멜케이스 적용(첫글자 대문자)
    - `CamelCase`
5. 메소드는 동사로 시작한다.
    - `run()`
6. 상수는 대문자와 언더스코어로 구성
    - `MAX_VALUE`

## 2.파일구조

1. 맨 위에 package명을 명시한다.
2. package에서 한줄 띄고 import 문을 작성한다.
3. import문에서 한줄 띄고 클래스명을 작성한다.

## 3.들여쓰기

1. 한 줄은 70자 이하로 한다.
2. 한 줄이 넘는 경우 다음과 같이 구성 한다.
    - 콤마 후에 두 줄로 나눈다.
    - 연산자(operator) 앞에서 두 줄로 나눈다.

```java
int func(int first, int second,
					int third)

return 1 + 2 + 3
				+ 4;
```

## 4.주석

1. 메소드 위에 작성
2. 변수 오른쪽에 작성
3. 기능에 대해 명세

## 5.문(statements)

1. 여는 중괄호 “{” 는 복합문을 시작하는 줄의 마지막에 위치해야 한다.
2. 닫는 중괄호 “}” 는 새로운 줄에 써야하고, 복합문의 시작과 같은 들여쓰기를 한다.
3. 조건/반복문은 항상 중괄호를 사용한다.(한줄도 중괄호 작성하기)

```java
if (condition) {
        statements;
    }
```

 

4. 빈 블럭에는 새 줄 없이 중괄호 닫기 허용

```java
public void close() {}
```

## 6.선언

1. 어노테이션 줄바꿈 후 작성

```java
@RequestMapping("/user")
public void finduser() {}
```

2. 한 줄에 한 문장

```java
int first = 0;
int second = 0;
```

3. 하나의 선언문에는 하나의 변수만

```java
int first;
int second;
```

4. 배열 대괄호는 앞쪽에 선언

```java
int [] arr;
```