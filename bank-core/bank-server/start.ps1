$libs = @()
$paths = @(
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\boot\spring-boot\2.7.18",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\boot\spring-boot-autoconfigure\2.7.18",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\spring-core\5.3.31",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\spring-context\5.3.31",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\spring-beans\5.3.31",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\spring-web\5.3.31",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\spring-webmvc\5.3.31",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\spring-jdbc\5.3.31",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\spring-tx\5.3.31",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\spring-aop\5.3.31",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\spring-expression\5.3.31",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\spring-jcl\5.3.31",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\spring-orm\5.3.31",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\spring-aspects\5.3.31",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\data\spring-data-commons\2.7.18",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\data\spring-data-jpa\2.7.18",
    "E:\AAA_CSWork\maven\maven_pro\org\apache\tomcat\embed\tomcat-embed-core\9.0.83",
    "E:\AAA_CSWork\maven\maven_pro\org\apache\tomcat\embed\tomcat-embed-el\9.0.83",
    "E:\AAA_CSWork\maven\maven_pro\org\apache\tomcat\embed\tomcat-embed-websocket\9.0.83",
    "E:\AAA_CSWork\maven\maven_pro\com\fasterxml\jackson\core\jackson-core\2.13.5",
    "E:\AAA_CSWork\maven\maven_pro\com\fasterxml\jackson\core\jackson-databind\2.13.5",
    "E:\AAA_CSWork\maven\maven_pro\com\fasterxml\jackson\core\jackson-annotations\2.13.5",
    "E:\AAA_CSWork\maven\maven_pro\com\fasterxml\jackson\datatype\jackson-datatype-jdk8\2.13.5",
    "E:\AAA_CSWork\maven\maven_pro\com\fasterxml\jackson\datatype\jackson-datatype-jsr310\2.13.5",
    "E:\AAA_CSWork\maven\maven_pro\com\fasterxml\jackson\module\jackson-module-parameter-names\2.13.5",
    "E:\AAA_CSWork\maven\maven_pro\com\fasterxml\jackson\dataformat\jackson-dataformat-yaml\2.13.5",
    "E:\AAA_CSWork\maven\maven_pro\com\baomidou\mybatis-plus-boot-starter\3.5.3.1",
    "E:\AAA_CSWork\maven\maven_pro\com\baomidou\mybatis-plus\3.5.3.1",
    "E:\AAA_CSWork\maven\maven_pro\com\baomidou\mybatis-plus-core\3.5.3.1",
    "E:\AAA_CSWork\maven\maven_pro\com\baomidou\mybatis-plus-extension\3.5.3.1",
    "E:\AAA_CSWork\maven\maven_pro\com\baomidou\mybatis-plus-annotation\3.5.3.1",
    "E:\AAA_CSWork\maven\maven_pro\org\mybatis\mybatis\3.5.13",
    "E:\AAA_CSWork\maven\maven_pro\org\mybatis\mybatis-spring\2.1.1",
    "E:\AAA_CSWork\maven\maven_pro\mysql\mysql-connector-java\8.0.33",
    "E:\AAA_CSWork\maven\maven_pro\com\zaxxer\HikariCP\4.0.3",
    "E:\AAA_CSWork\maven\maven_pro\org\slf4j\slf4j-api\1.7.36",
    "E:\AAA_CSWork\maven\maven_pro\ch\qos\logback\logback-classic\1.2.12",
    "E:\AAA_CSWork\maven\maven_pro\ch\qos\logback\logback-core\1.2.12",
    "E:\AAA_CSWork\maven\maven_pro\org\projectlombok\lombok\1.18.30",
    "E:\AAA_CSWork\maven\maven_pro\cn\hutool\hutool-all\5.8.22",
    "E:\AAA_CSWork\maven\maven_pro\jakarta\annotation\jakarta.annotation-api\1.3.5",
    "E:\AAA_CSWork\maven\maven_pro\org\yaml\snakeyaml\1.30",
    "E:\AAA_CSWork\maven\maven_pro\org\apache\logging\log4j\log4j-api\2.17.2",
    "E:\AAA_CSWork\maven\maven_pro\org\apache\logging\log4j\log4j-to-slf4j\2.17.2",
    "E:\AAA_CSWork\maven\maven_pro\org\slf4j\jul-to-slf4j\1.7.36",
    "E:\AAA_CSWork\maven\maven_pro\org\aspectj\aspectjweaver\1.9.7",
    "E:\AAA_CSWork\maven\maven_pro\org\hibernate\validator\hibernate-validator\6.2.5.Final",
    "E:\AAA_CSWork\maven\maven_pro\javax\validation\validation-api\2.0.1.Final",
    "E:\AAA_CSWork\maven\maven_pro\org\jboss\logging\jboss-logging\3.4.3.Final",
    "E:\AAA_CSWork\maven\maven_pro\com\fasterxml\classmate\1.5.1",
    "E:\AAA_CSWork\maven\maven_pro\com\github\jsqlparser\jsqlparser\4.5",
    "E:\AAA_CSWork\maven\maven_pro\org\apache\commons\commons-lang3\3.12.0",
    "E:\AAA_CSWork\maven\maven_pro\commons-logging\commons-logging\1.2",
    "E:\AAA_CSWork\maven\maven_pro\commons-codec\commons-codec\1.15",
    "E:\AAA_CSWork\maven\maven_pro\org\apache\velocity\velocity-engine-core\2.3",
    "E:\AAA_CSWork\maven\maven_pro\org\reactivestreams\reactive-streams\1.0.4",
    "E:\AAA_CSWork\maven\maven_pro\org\checkerframework\checker-qual\3.37.0",
    "E:\AAA_CSWork\maven\maven_pro\com\google\code\findbugs\jsr305\3.0.2",
    "E:\AAA_CSWork\maven\maven_pro\jakarta\inject\jakarta.inject-api\2.0.1",
    "E:\AAA_CSWork\maven\maven_pro\javax\persistence\javax.persistence-api\2.2",
    "E:\AAA_CSWork\maven\maven_pro\jakarta\persistence\jakarta.persistence-api\3.1.0",
    "E:\AAA_CSWork\maven\maven_pro\jakarta\transaction\jakarta.transaction-api\2.0.1",
    "E:\AAA_CSWork\maven\maven_pro\org\hibernate\orm\hibernate-core\6.2.13.Final",
    "E:\AAA_CSWork\maven\maven_pro\org\hibernate\common\hibernate-commons-annotations\6.0.6.Final",
    "E:\AAA_CSWork\maven\maven_pro\org\jboss\logging\jboss-logging\3.5.3.Final",
    "E:\AAA_CSWork\maven\maven_pro\io\smallrye\jandex\3.0.5",
    "E:\AAA_CSWork\maven\maven_pro\org\glassfish\jaxb\jaxb-runtime\2.3.9",
    "E:\AAA_CSWork\maven\maven_pro\com\sun\istack\istack-commons-runtime\3.0.12",
    "E:\AAA_CSWork\maven\maven_pro\jakarta\xml\bind\jakarta.xml.bind-api\2.3.3",
    "E:\AAA_CSWork\maven\maven_pro\jakarta\activation\jakarta.activation-api\1.2.2",
    "E:\AAA_CSWork\maven\maven_pro\com\sun\activation\jakarta.activation\1.2.2",
    "E:\AAA_CSWork\maven\maven_pro\org\dom4j\dom4j\2.1.4",
    "E:\AAA_CSWork\maven\maven_pro\org\glassfish\jaxb\jaxb-core\2.3.0.1",
    "E:\AAA_CSWork\maven\maven_pro\javax\xml\bind\jaxb-api\2.3.1",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\boot\spring-boot-starter\2.7.18",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\boot\spring-boot-starter-web\2.7.18",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\boot\spring-boot-starter-tomcat\2.7.18",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\boot\spring-boot-starter-json\2.7.18",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\boot\spring-boot-starter-logging\2.7.18",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\boot\spring-boot-starter-validation\2.7.18",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\boot\spring-boot-starter-jdbc\2.7.18",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\boot\spring-boot-starter-aop\2.7.18",
    "E:\AAA_CSWork\maven\maven_pro\org\springframework\boot\spring-boot-starter-data-jpa\2.7.18"
)

foreach ($p in $paths) {
    if (Test-Path $p) {
        $jar = Get-ChildItem $p -Filter "*.jar" | Select-Object -First 1
        if ($jar) {
            $libs += $jar.FullName
        }
    }
}

$libs = $libs | Select-Object -Unique
$cp = $libs -join ";"
$cp += ";E:\Agent\MBCS\bank-core\bank-server\target\classes"
$cp += ";E:\Agent\MBCS\bank-core\bank-liability\target\classes"
$cp += ";E:\Agent\MBCS\bank-core\bank-customer\target\classes"
$cp += ";E:\Agent\MBCS\bank-core\bank-teller\target\classes"
$cp += ";E:\Agent\MBCS\bank-core\bank-common\target\classes"

$env:CLASSPATH = $cp
Write-Host "Starting BankServerApplication..."
java com.bank.server.BankServerApplication
