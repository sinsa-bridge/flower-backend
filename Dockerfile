# 기본 이미지로 OpenJDK 11을 사용합니다.
FROM openjdk:11

# 애플리케이션 JAR 파일을 복사할 디렉토리를 생성합니다.
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# 컨테이너의 내부 포트를 지정합니다. (프로젝트에서 사용하는 포트로 변경)
EXPOSE 7737

# JAR 파일을 실행하는 명령을 지정합니다.
ENTRYPOINT ["java", "-jar", "/app.jar"]
