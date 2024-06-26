# - English
# Dyslexia-Zoo
### Introduction
Dyslexia-Zoo is a capstone design project at the Chung-Ang University's Department of Software Engineering. Please note that we will not be accepting contributions for Dyslexia-Zoo, as it is a Capstone Design Project.

#### Dyslexia 
An inability to read or spell words accurately and fluently in children or adults who do not notice any significant difficulty in listening or speaking.
A type of learning disability

#### Phonological Dyslexia
Dyslexia in which problems with phonological awareness lead to difficulties with letter acquisition and word decoding.

#### Fluency Dyslexia
Dyslexia in which a person has no difficulty reading words but has poor fluency and comprehension of content.

### The Goal Of Dyslexia-Zoo Project 
1. Services for children with dyslexia.
2. For The Children who has Phonological Dyslexia.
3. For The Children who has Fluency Dyslexia. 

### Dyslexia Zoo Project Architecture
<img width="100%" src="https://github.com/Capstone-team02-Dyslexia-Cure-Service/Dyslexia-Ml-Server/assets/106421292/648f08de-1a15-4f9c-a638-0a7a753e8b84"/>

### Sub Projects of Dyslexia-Zoo
* [Dyslexia-Zoo-FE](https://github.com/Capstone-team02-Dyslexia-Cure-Service/Dyslexia_Zoo_FE)
    * Dyslexia-Zoo-FE - react
* [Dyslexia-Zoo-BE](https://github.com/Capstone-team02-Dyslexia-Cure-Service/Dyslexia-BE)
    * Dyslexia-Zoo-BE - spring
* [Dyslexia-Zoo-ML](https://github.com/Capstone-team02-Dyslexia-Cure-Service/Dyslexia-Ml-STT)
    * Dyslexia-Zoo-ML - jupyterNotebook
* [Dyslexia-Zoo-ML Server](https://github.com/Capstone-team02-Dyslexia-Cure-Service/Dyslexia-Ml-Server)
    * Dyslexia-Zoo-ML Server - Flask

# How To Execute Back-End Server(Spring)
1. Install jdk 17
2. Run MySQL
3. Create gitignore file - `application-aws.yml` in to `src\main\resources`. Each private variable(eg. `spring.datasource.datasource.url`) should have an appropriate value.
   </br>
   ![image](https://github.com/Capstone-team02-Dyslexia-Cure-Service/Dyslexia-BE/assets/53044223/8300b330-c820-423c-a80a-a33568f1c4b3)
   </br>
   - `application-aws.yml`
      ```
      spring:
        datasource:
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: YOUR_MYSQL_ENDPOINT
          username: YOUR_MYSQL_USER_NAME
          password: YOUR_MYSQL_PASSWD
      
      # 환경 변수
      cloud:
        aws:
          credentials:
            accessKey: YOUR_AWS_ACCESS_KEY
            secretKey: YOUR_AWS_SECRET_KEY
          region:
            static: ap-northeast-2  # seoul region
          s3:
            bucket: YOUR_S3_BUCKET_NAME
            # you should configure s3 bucket directory as below
            path:
              pronunciation: pronunciation
              video: video
              submission: submission
      
      flask:
        api:
          url: YOUR_ML_SERVER_IP_ADDR
      
      logging:
        discord:
          webhook-uri: YOUR_DISCORD_WEB_HOOK_URL
        config: classpath:logback-spring.xml
        level:
          org:
            hibernate:
              type:
                descriptor:
                  sql: error
      
      
      
      cors:
        add-mapping: /**
        allowed-origins:
          allowed-origin-localhost-1st: YOUR_1ST_ALLOWED_ORIGIN
          allowed-origin-localhost-2nd: YOUR_2ST_ALLOWED_ORIGIN
          allowed-origin-frontend: YOUR_FRONT-END_WS_ALLOWED_ORIGIN
        allowed-methods:
          - GET
          - POST
          - PUT
          - DELETE
          - OPTIONS
        allowed-headers:
          - Origin
          - X-Requested-With
          - Content-Type
          - Accept
          - Authorization
          - memberId
          - animalId
          - numOfQuestions
          - solvingRecordId
          - questionId
          - questionResponseType
          - questionType
        allow-credentials: true
        max-age: 3600
      
      ```
5. Run main method in DyslexiaApplication.java!
---
# - 한글
# Dyslexia-Zoo
### 소개
Dyslexia-Zoo는 중앙대학교 소프트웨어학부의 캡스톤 디자인 프로젝트입니다. Dyslexia-Zoo는 캡스톤 디자인 프로젝트이기 때문에 기부금을 받지 않습니다.

#### 난독증
학습장애의 일종으로 단어를 정확하고 유창하게 읽거나 철자를 적는 것은 듣는 것이나 말하는 것에 큰 어려움을 느끼지 못하는 어린이나 성인에게 발생합니다.

#### 음운성 난독증
음운 인식의 문제가 글자 획득 및 단어 해독의 어려움으로 이어지는 난독증.

#### 유창성 난독증
단어를 읽는 데는 어려움이 없지만 내용에 대한 유창성과 이해력이 떨어지는 난독증.

### Dyslexia Zoo 프로젝트 목표
1. 난독증 어린이를 위한 서비스입니다.
2. 음운성 난독증을 앓고 있는 어린이들을 위한 서비스입니다.
3. 유창성 난독증을 앓고 있는 어린이들을 위한 서비스입니다.

### Dyslexia Zoo Project Architecture
<img width="100%" src="https://github.com/Capstone-team02-Dyslexia-Cure-Service/Dyslexia-Ml-Server/assets/106421292/648f08de-1a15-4f9c-a638-0a7a753e8b84 "/>

### Dyslexia Zoo Sub Projects
* [Dyslexia-Zoo-FE] (https://github.com/Capstone-team02-Dyslexia-Cure-Service/Dyslexia_Zoo_FE)
   * Dyslexia Zoo Front-End(React)
* [Dyslexia-Zoo-BE] (https://github.com/Capstone-team02-Dyslexia-Cure-Service/Dyslexia-BE)
   * Dyslexia Zoo Back-End(Spring)
* [Dyslexia-Zoo-ML] (https://github.com/Capstone-team02-Dyslexia-Cure-Service/Dyslexia-Ml-STT)
   * Dyslexia Zoo ML Model(jupyterNodtebook)
* [Dyslexia-Zoo-ML Server] (https://github.com/Capstone-team02-Dyslexia-Cure-Service/Dyslexia-Ml-Server)
   * Dyslexia Zoo ML Server(Flask)
# 실행 방법
1. jdk 17 설치
2. MySQL 실행
3. `src\main\resources`에 gitignore file - `application-aws.yml` 만들기. yml파일 내에 private 변수(eg. `spring.datasource.datasource.url`)에 적절한 값 넣어주기.
   </br>
   ![image](https://github.com/Capstone-team02-Dyslexia-Cure-Service/Dyslexia-BE/assets/53044223/8300b330-c820-423c-a80a-a33568f1c4b3)
   </br>
   - `application-aws.yml`
      ```
      spring:
        datasource:
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: YOUR_MYSQL_ENDPOINT
          username: YOUR_MYSQL_USER_NAME
          password: YOUR_MYSQL_PASSWD
      
      # 환경 변수
      cloud:
        aws:
          credentials:
            accessKey: YOUR_AWS_ACCESS_KEY
            secretKey: YOUR_AWS_SECRET_KEY
          region:
            static: ap-northeast-2  # seoul region
          s3:
            bucket: YOUR_S3_BUCKET_NAME
            # you should configure s3 bucket directory as below
            path:
              pronunciation: pronunciation
              video: video
              submission: submission
      
      flask:
        api:
          url: YOUR_ML_SERVER_IP_ADDR
      
      logging:
        discord:
          webhook-uri: YOUR_DISCORD_WEB_HOOK_URL
        config: classpath:logback-spring.xml
        level:
          org:
            hibernate:
              type:
                descriptor:
                  sql: error
      
      
      
      cors:
        add-mapping: /**
        allowed-origins:
          allowed-origin-localhost-1st: YOUR_1ST_ALLOWED_ORIGIN
          allowed-origin-localhost-2nd: YOUR_2ST_ALLOWED_ORIGIN
          allowed-origin-frontend: YOUR_FRONT-END_WS_ALLOWED_ORIGIN
        allowed-methods:
          - GET
          - POST
          - PUT
          - DELETE
          - OPTIONS
        allowed-headers:
          - Origin
          - X-Requested-With
          - Content-Type
          - Accept
          - Authorization
          - memberId
          - animalId
          - numOfQuestions
          - solvingRecordId
          - questionId
          - questionResponseType
          - questionType
        allow-credentials: true
        max-age: 3600
      
      ```
5. DyslexiaApplication.java에 main method 실행!
