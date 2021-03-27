/* Workbenche daxil oldug kecen ders yazdigimiz table-lardan school_student adini yalnis yazmisig deyisib  edirik
student_teacher. Bunlar arasinda elaqe many to many idi.Google da jpa manyto many yazib "vladmihalcea" saytinda
numuneye baxirig burda post ile tag arasinda elaqe qurulub post_tag vasitesile burdaki numuneden oz table-larimiz ucun
istifade edirik proyektimizde.Application daxil olub entity paketinde birdene TeacherEntity  (teacher table-na uygun)
clasi acirig-
@Entity(name="teacher")
public class TeacherEntity {
    @Id
    private Integer id;
    private String name;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
            CascadeType.REMOVE
    })
    @JoinTable(name = "student_teacher",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<StudentEntity> studentList;
}
Clasin uzerinde @Entity ile baxdigi table-i qeyd etdik,  @ManyToMany ve  @JoinTable numunesini adini cekdiyimiz saytdan
goturduk bu table-lari birlesdiren table-imizin adini yazdig -name = "student_teacher",  ve oldugumuz table-in id sini
yazdig -name = "teacher_id" , teacher table-nin id-si  ile student table-in id-si join olub onuncunde inverse-de
name = "student_id" yazdig.
Burda var cascade bu onuncundur ki, meselen biz teacher-de studentlist acib bura studentleri yigirig
(table-da yox obyektde) ve persist var yeni teacheri (save) - insert edende avtomatik telebeleri de insert edir,
jdbc-de ise teacheri insert edende dene dene studentleri de insert edirdik, hemcinin merge - update ucun, remove-da
silmek ucundur.Yeni tutalim 4 nomreli muellim silinibse buna da 5 nomreli telebe join olub oda avtomatik silinir.
Yeni bu telebeni student_teacher table-inda silir, student table-inda deyil, yeni 7 nomreli telebe 1 nomreli muellime
baxir 1 nomre silinende o setr tam silinir avtomatik telebede silinir.Eyni bu mentiqi StudentEntity clasinda da tetbiq
edirik, goturduk-
 @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
            CascadeType.REMOVE
    })
    @JoinTable(name = "student_teacher",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<TeacherEntity> teacherList;
yazdig, burda da teacherlist yazirig.Student_teacher table-inda bu clasi ifade eden student_id-dir, bu id vasitesile
join olub teacher_id-e.
student ile school arasinda many to one elaqesi, school ile student arasinda ise one to many elaqesi var.Bir
SchoolEntity clasi acirig ve icinde telebeler olmalidir eger bir nomreli school-u cekdinse onun telebelerinde goture
bilmelisen ve one to many elaqesi var -
@Entity(name="school")
public class SchoolEntity {
    @Id
    private Integer id;
    private String name;

    @OneToMany
    private List<StudentEntity> studentList;
}
Eyni sey studententity -de edirik school acirig
 @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolEntity school;
burda bir mektebe baxir deye list yazmirig ve many to one olur student-den mektebe.Bu numunelere(elaqelere) adini
cekdiyimiz saytdan baxirig.
@JoinColumn(name = "school_id") - bu ise deyirki student table-indaki school_id ile join oluram school table-na.Burda
cascade lazim deyil, cunki sabah student table-inda
bir student-e school_id yazirig ve bele bir school yoxdu tutalim ancag student-i insert edende avtomatik bele bir
school qurub set edecek(cascdade persist) bu telebeye bu duzgun deyil burda xeta vermesi daha meqbuldu, acnag bu
student_teacher-de bele deyil yeni orda bir telebe qurub, ona bir student set edende olmayan student-i qurmag deyil bu
ve bu gelir student_teacher-de hemin telebeleri bura join edir, burda  set edir yeni.School_id qirmizi verir gormemeyin
sebebi assign data source gelir uzerine mouse getirende.Bunu aradan qaldirmag ucun- sagda database -> + ->
data source +> MySql daxil olub user: root, parol: 1 (8 dene),
url : jdbc:mysql://127.0.0.1:3306/librarymanagementsystemdb?&serverTimezone=UTC
sonra test connection ve apply edirik ve uzerine gelib qosuldugu xanani clikcleyirik sadece yeni -
librarymanagementsystemdb xanasini ve qirmizilar itir.
Student_Teacher(bir birimize join oldugumuz clas)-in student_id-si student-e baxir yeni entity clasina, teacher_id
qarsi terefe baxir, inverse kime join olmusansa onun columnu ifade edir bu hemcinin TeacherEntity-de beledi tersi
olarag.
Student clasinda school deyisen uzerinde @JoinColumn var ancag  School clasinda yoxdur, table-da da baxsaq gorerik
yoxdur cunki eslinde student school-a join olur ona gore qeyd etmisik ki studentin icinde biz school-a school_id ile
join oluruq, sadeece schoolda niye @onetoone ve studentlist yazmisig- biz school-u cekende gedecek asagida adini
verdiyimiz clas adina baxacag StudentEntity-e ve ordan table-na baxacag ki bu table school-a join olub belelikle
school-u cekende listide doldurub verecek.
One to one elaqesi ucun SchoolAddress clasini qururug ve deyisenleri yazib getter setter metodlarin yazdig-
@Entity(name="school_address")
public class SchoolAddress {
    @Id
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

SchoolEntity ve TeacherEntity claslarinda da getter setterleri yazirig (StudentEntity clasinda onceden variydi).
SchoolAddress clasina bele bir deyisen  yazib join edirik-
 @OneToOne
    @JoinColumn(name="school_id")
    private SchoolEntity school;
cunki numuneye baxdig saytdan, bizim postdetail-imiz SchoolAddres-dir.ve bu clasin school_id-si join olur school-a.
SchoolEntity clasinda schooladdress adli deyisen yazirig-
@OneToOne(mappedBy = "school", cascade = CascadeType.ALL)
    private SchoolAddress schoolAddress;
burda cascade ona gore yazdig ki mekteb qurulur address-de qurulur mekteb silinirse avtomatik adresde silinmelidir.
Mappedby SchoolAddress-in icinde(yeni clasin icindeki) school deyisenini isteyir. Join column deyende column adin
verirsen ancag mappedby deyende deyisenin adini verirsen.Yeni SchoolAddress clasin icindeki bu deyisen vasitesile bir
birimize join olmusug.
Texniki olarag mysql-de icaze vermir onsuz mekteb silinende adres qalsin.Indi bunlari proyektde qosurug controller-de
studentleri cekirik, once StudentEntity clasinda sonradan yazdigimiz TeacherList ile school deyisenin de getter
setter-in acirig.Sonra controller-de index metodun daxilinde listden sonra for each acirig ve capa veririk.
List<StudentEntity> list= studentService.findAll(
                name,
                surname,
                age,
                scholarship
        );
        for(StudentEntity l: list){
            System.out.println(l.getTeacherList());
            System.out.println(l.getSchool());
        }
        model.addAttribute("list",list);
        return "students/index";
    }
Sonra capa verende problem olmasin deye TeacherEntity clasinda toString metodunu acirig ve student-i silirik ona gore ki
StudentEntity-in icinden teacher-i capa vereceyik birde niye student capa verek burda.Birde SchoolEntity clasinda
toString acag burda da yalniz id, name bes edir tostring-de.Run etdik ancag TeacherList ve school-u capa vermisik
student-i yox, onuncun bilinmir hansi muellim hansi telebeye baxir.Workbench-de teacher table-inda muellim adlarin
deyisirik orda 2 dene muellim variydi onlari id 1-i John , id 2-ni Bean yazdig, Ancag capda verilen muellimi hansi
telebeye baxdigini bilmek ucun view-da siyahiya baxarag bilmek olar ki birinci TeacherEntity ilk setrdeki student-e
baxir -

[TeacherEntity{id=1, name='John'}, TeacherEntity{id=2, name='Bean'}]
SchoolEntity{id=1, name='6 nomreli mekteb'}
[TeacherEntity{id=1, name='John'}]
SchoolEntity{id=2, name='5 nomreli mekteb'}
[]
SchoolEntity{id=1, name='6 nomreli mekteb'}
[]
SchoolEntity{id=1, name='6 nomreli mekteb'}
[]
SchoolEntity{id=1, name='6 nomreli mekteb'}
Capda da gorunduyu kimi birinci setrde gelen telebeye John ve Bean adli muellim baxir ve 6 nomreli mektebdedir, Ikinci
setrdeki telebeye yalniz John muellimi baxir ve 5 nomreli mektebde oxuyur, diger telebelere muellim verilmeyib
Workbenchde de student_teacher table-na baxsag gorerik  ancag school_id-si verilib student table-na baxsag gorerik
hemcinin.
Indi bunu cixarag html-e muqayise etmek daha rahat olsun, bu arada Templates-de bir dene teacher folderi acmisig.
Students folderinde index.html-de table-in thead icinde scholarship ile controls arasina school ve teachers artirdig-
<th>scholarship</th>
<th>school</th>
<th>teachers</th>
<th>controls</th>

sonra table-in tbody-sin tr icinde scholarshipden sonra yazirig-

<td th:text="${student.scholarship}">200</td>
<td th:text="${student.school.name}">schoolname</td>
<td th:each="teacher: ${student.teacherList}">
       <span> th:text="${teacher.name}"</span>
</td>

run edib browsere baxsag goreceyik ki bir problem olduilk setrdeki telebenin 2 muellimi var ve burda ikinci muellim
controls-un altina dusur, cunki biz td-ni for eache qoymusug(teacherList-i) ve bunu for eachden cixarib span icinde for
eache qoyurug , icinde birdene de span qurub teachername ora yazirig-
<td th:text="${student.school.name}">schoolname</td>
<td>
      <span th:each="teacher: ${student.teacherList}">
              <span th:text="${teacher.name}"></span>
       </span>
</td>
bele olanda td repeat olmur span repeat olur.Span- hec kese mudaxile etmeyen sirf rap ede bileceyin, bu cure element
kimi istifade edeceyin bir tagdir.Bunun hec bir boslugu, dizayni,css-i, mesafesi yoxdur, sadece elementdir lazimsa
istifade ele, div olsaydi onun yerinde avtomatik enter ederdi yeni bura oturur novbeti komponenti atir avtomatik
asagiya.
Update edende (edit configurations-da) programi sondurur tezden yandirir, hot swap ise sirf deyisdiyin seyleri update
edir ancag bu bezen problem verir yeni 2 clasda deyisiklik edirsen bu zaman bezen birini update edir digeri qalir.
Muellimde autodeploy olmurdu edit configurations-da update classes secib apply etdi  appin adin deyisdi hemcinin (2
elave etdi) evvelki adda spring boot oldugu ucun.
Workbench-de teacher table-na Jack adli muellim elave edib  3 setrdeki student-e join edirik student_teacher-de ve
browserde de elave olunur.Controllerde bayag yazdigimiz index metodundaki for meselesini  silirik bunu tetbiq etib
anladig, basqa movzulara baxag.
Tranzaksiya meselesi- demeli biz StudentRepository-de melumatlari cekir  ancag emeliyyati sonlandirmir yeni
tranzaksiyani baglamir, baglanmadigi ucunde biz gelib html-de deyendeki men studentin school-nun adini isteyirem -
<td th:text="${student.school.name}">schoolname</td>  bazaya yeniden request gedir ve ad tapilib , capa verilir. Lakin
StudentRepository-nin uzerine @Transactional yazsag tranzaksiya acilir ve StudentRepository-den bir defe cagirandan
sonra tranzaksiya baglanir sonra html-de schoolun adin isteyende xeta verir ki tranzaksiya baglidir, xetani almamag ucun
@Transactional bunu yazmamaliyig.

StudentEntity-de manytoone-da cascade-den yuxari  fetchType yazirig-

@ManyToMany(fetch = FetchType.LAZY,
            cascade = {
bu o demekdir ki biz student-i cekende icindeki teacherlist-i cekmir ,lazy -tenbeldir, lazim olanda cekir.Lazy yerine
EAGER da yazilir bu ise studenti cekende muellimleri de cekir.Bizim veziyyete EAGER lazimdi her defe bazaya muraciet
etmesin deye studenti cekende teacher-i de ceksin.Lazy de ise her defe 10 student olsa 10 defe bazay sorgu gedecek
muellimlerini dartmag ucun, mekteblerini dartmag ucun. defaul olarag(hecne yazmasag) lazy olur.
Cascade-i yoxluyag - browserde 3 cu setrdeki telebeni silirik ve workbenchde student_teacher de goruruk ki 3 cu setrdeki
telebeye join olan muellimde silinir halbuki biz student-i sil demisik student-teacherden sil komandasi vermemisik.Bu
jpa-nin imkanidi spring datadan yox jpa-nin ozunden gelir cascadeType baxanda gorerik ki javax.persistenceden gelir.
Enable print sql (sql-in capa verilmesi) - yeni biz deyirik ki sql-i bizim evezimizden  generasiya edir , hansi sql-i
generasiya etdiyini gormek isteyirikse enable edirik print sql-i o bize gosterir.Bununcun google-da spring boot jpa
enable sql logging yazirig ve stackoverflow-dan numune gotururuk, burda 2 dene sey var-
1) logging.level.org.hibernate.SQL=DEBUG    ve
2) spring.jpa.show-sql=true
2 ci konfiqurasiya daha ustundu cunki bu jpa-dir bununla jpa uzeri butun kitabxanalari, meselen eclipselinki de
aktivlersdire bilersen ancag birinci ancag hibernate ucundur.2ci goturub proyektde application.properties-de yazdig.
Burda olan spring sozu spesifikasiya uzre gedir lakin spring data-ya aid olan bir seydir.Yeni bu kodu bura yazmagla
spring data avtomatik olarag jpa-da yazmali oldugumuz kodlar ki var (muellim gosterdiyi - hansiki kohnede bele yazilir
dediyi) onlari ozu yazir, bu da spring boot-un bize vermis oldugu imkandir.Birde jpa auto generate all tables var bunu
da enable edeceyik.
Jpa auto generate all tables - table-lari silsek (workbenchde) proyekti ayaga qaldiranda yeniden table-lar qurulur.Bunu
da spring boot vasitesile ede bilerik.
Google-da spring boot auto generate tables yazirig ve stackoverflow-da baxirig, bir nece usullar var hibernate ucun ,
jpa ucun, bir parametr var hetta -createDatabaseIfNotExist=true bu jpa deyil sirf mysql-in parametridir.Mysql-e
qosulanda deyisen ki bu database qur ve ona qosul. 2 dene komanda yazirig application.properties-de
1) spring.jpa.generate-ddl=true
2) spring.jpa.hibernate.ddl-auto=update (bunun umumi jpa ucun  olanini tapa bilmedik)
birinci komanda deyirki qur table-lari, ikinci komanda ise deyir yoxdusa qur varsa update et, meselen 3 dene column-lu
table-in var 4 cunu sonradan elave etmisen bu zaman update 3-nu update edir 4 cunu elave edir.
2 ci komandanin update-den elave diger 3 type-da var 1)create(yoxdursa create et), 2)create-drop(varsa sil yeniden qur,
bu risklidir cunki 1000 data olar silib tezeden quracag buda duzgun bir sey deyil, amma sirf test aparanda bu lazimdir
meselen senin 3 setrin var ve delete-i test etmek isteyirsen belelikle table-i silirsen tezeden qurub ora 3 setr elave
edirsen ve qaranti olursan menim 3 setrim var sonra ordan bir setri silib yoxlayirsan 2 dene setr qaldimi? 2 dene
qaldisa demek 1 ni silmisen ve senin silme funksiyan isleyir. Bu silmeni test etmek ucundur bu zaman create-drop
menalidir ki proyekt ayaga qalxanda butun table-lari silir yeniden qurur ve biz bilirik ki sifir bazayla ise baslamisig,
bazada sirf senin istediyin datalar insert olur ve biz isteydiyim testi aparirig),
3)validate ( validasiya et gore table-lar var yoxsa yox).
"telebe sual verirki men anlaya bilmirem bu kecilen movzular ne demek isteyir? muellim izah edirki biz student-i
cekende repository qururdug ve ele cekirdik indi ise bir komanda ile ona join olan mektebleri, muellimleri de cekirik.
Jdbc ile ise etseydik elave sql qurulacagdi, eger workbenchde student silseydik diger table-larda studentin adi kecdiyi
ucun onlari da orda silmeli olardig, tutalim 50 table-da adi kecib studentin gedib adi kecen yerde setrleri silib sonra
student table-inda gelib sile bilerdik belelikle cascade avtomatik bizim eveziminden etdi. Hemcinin evvel her birini
dartmag ucun query yazmali idik tutalim student-e aid school-u ucun yazmali idin getschool by studentId ve s.
indi ise birini dartmagla hamisini goturduk.Bir sozle verdiyi imkan odurki bir datani cekende ona aid olan datalari
elave query yazmdan cekir."
Yazdigimiz funksiyalari yoxluyag , workbench-de silirik table-lari. update-in dezavantaji odurki qurur yeniden
table-lari ancag icinde datalari olmur.Yene de musbet haldi cunki app hazir edib istifadeye verende datalar olmur bos
verirsen , avtomatik table-lar qurulur istifade edende.Bir musbet ceheti de odurki sabah mysql yox oracle istifade ede
de bilirsen gelib sadece application.properties de datasource.url-e mysql yox oracle-in url-ni yazirsan ve driver
olarag da mysql yox oracle driver-i istifade edirsen bitdi daha oracle falan query-si yazmag lazim deyil avtomatik
yazilir.Workbenchde table-lari silmek ucun secirik butun table-lari sag click drop 6 tables... secdik review sql click,
acilan query-ni copy edib yuxarda yazib run edirik ve table-lar silindi.
Application-a gelib restart(run) edirik.
Birde avtomatik generasiya meslehet deyil tutalim sen tehsil nazirliyinin proyektini isleyirsen create drop yazmisan
ele bil butun Azerbaycan telebelerinin melumatlarini silirsen table-i tezeden qurursan bir sehve gore bu basa vere
biler ona gore deyilir ki proction-da istifade etmeyin.Elave olarag hansisa table-da sutun adi deyismisen bu deyisse
bele yene production-da, database-de problem cixara biler.Cunki hemen sutun adini basqa-basqa table-lar istifade edir
ve hemen sutun yoxdu deye qirilmalar olacag.Yeni tutalim StudentEntity-e gelmisen surname-i deyisib soyad yazmisan ve
gedir avtomatik soyad sutunu qurulur ve soyad-a insert edirsen neyi insert edirsense surname-e yox.Belelikle ekranda
surname-i gostermeye calisacag halbuki sen artig  soyad-a insert edirsen.Ona gore bu avtomatik generasiyani
production-da istifade etmirler hansisa sutun, table qurulmalidirsa database admin-e deyirler o qurur teqdim edir ve
sende koduva hemen table-i falan tetbiq edirsen.
Run oldu proyekt ve workbenchde tables-a gelib refresh veririk table-lar gelir(student, teacher, school,
student_teacher, school_address, school-student_list).
Burda butun table-lar duz qurulub ancag school_student_list duz deyil biz clasda onetomany demisik ancag bu table
manytomany-dir.Bu table artigdir cunki biz school da studentler cek deyende gedir student table-in school_id- sinden
cekir.Bunu arasdirmag olar google-da onetomany quranda manytomany-de qurulur bu nece iqnor etmek olar bele arasdirib
tapmag olar ancag bize mane olmur hazirda.
Capda baxsag gorerik sql print olub yeni query-ler cap olub, create table , alter(update demekdir) table ve s.Sonda
bele bir query generasiya edir bunu copy edib workbenchde baxag-
select studentent0_.id as id1_3_, studentent0_.age as age2_3_, studentent0_.name as name3_3_,
studentent0_.scholarship as scholars4_3_, studentent0_.school_id as school_i6_3_,
studentent0_.surname as surname5_3_
from student studentent0_
Burda deyir select student ve hansi table-dan ki cekib buna ad verib - studentent0_,
buna alyas deyirler.
Alyas yeni ki select * from student s- s desek sona gerek evvelde select s.* demeliyik ele bil student-e s gozuyle
baxirig. ardiyca meselen where s.name='asd' falan deye bilerik artig student evezine s yaza bilerik yeni.
Burda da beledir deyir bu alyasin id-si age-ni cek ve her deyisene de alyas verir, buda hibernate-in oz isidir bucur
implementasiya edibler.Burda 1, 2,3 falan indexlerde verir ozu bele edirki bunlari cekende hemen indexler uzre set
elesin obyekti cox guman.
Bu hibernate ucun olan query-dir eclipselinkde ferqlidir ve hibetnate-in query-si daha performanslidir bizim
kodumuzunda buna tesiri var yeni nece - sen orm isledirem deyib agliva gelen kimi yazsan yene de performansda sql
generasiya olunmayacag bu jpql dersindedir.
Jpql (java persistence query language) nedir- burda query uzu gorursen ancag obyekt yonumlu query.
Tutalim bir StudentRepository-de bir dene metod ile schoolId ve age gore studentleri cekib liste yigirig bunu spring
data ile de yaza bilirik hansiki evvel elemisik ancag ele bir veziyyet olar ki cox ozel bir query olar bucur yaza
bilmerik o zaman bele edirik-

@Query(value= "select s from student s where s.school.id=:id and s.age=:age")
    List<StudentEntity> findAllBySchoolIdAndAge(@Param("id") Integer id, @Param("age") Integer age);

demeli metodun basina @query yazirig sonra table-in adin cekmirik entity adini yazirig , entity-in adi student.dir ve
alyas qoyurug (s). birbasa s yazdig * qoymadig, where den sonraya baxsag gorerik obyekt yonumlu gedir, sonra @Param
yazirig id- id-ye oturacag age de age-e oturacag.Ancag bu query sql deyil jpql-dir. Jpql-in avantaji odur ki biz yene
jpa ile yazdig, database deyisende mysql de bir cure ola biler onun neticesi, oracle-da basqa cure.Ona gore biz query-ni
print etdik ki, (spring.jpa.show-sql=true bununla) yazdigimiz query-nin - jpql hansi query-ni generasiya etdiyini gorek.
meselen date olabiler s.birtdate<:birtdate bu date ele bir anlayisdir ki bazadan bazaya deyisir meselen oracle-de
to_date, postql-de sadece date ve s. ola biler.Belelikle burda jpql yazmagla ozu nece lazimdirsa bazaya gore avtomatik
generasiya edir.
Sql-i jpa-da istidafe- bezen ele queryler olur onu jpql-de yazmag ele rahat olmur, ya query yazan ayri olur javada
isleyen ayri, bu zaman nativequery (bildiyimiz sql) yazirig. Bu zaman jpql istifade ede bilmirsen spring metod ile
yazilmis oldugu query-sini istifade ede bilmirsen, sirf query yazmalisan.Bes nece ede bilerik ki native query yazag
ancag yenede obyekt yonumlu olsun bize StudentEntity qayitsin, yeni ozumuz map etmeyek, jdbc-deki kimi datani goturub
dene dene etmeyek.Bu zaman goturek ele bayag yazdigimiz metodun query-sini misal olarag. Buna artiririg native query
onuncun ulduz elave edirik school.id -nide school_id edirik -

 @Query(nativeQuery = true, value= "select s.* from student s where s.school_id=:id and s.age=:age")
    List<StudentEntity> findAllBySchoolIdAndAge2(@Param("id") Integer id, @Param("age") Integer age);

Sari verdi cunki native query yazmisig ancag hansi bazaya uyugundu onu dememisik yeni mysql-dir?, oracle-dir? kursoru
sari-nin uzerine getiririk change dialect to... click edirik sonra global sql dialect-de mysql secirik hemcinin project
sql dialect-de de.Intellij dialecti ozu render edir yeni mysql secmisen ancag oracle kodu yazmisan bu sehvi gosterir
sene.Biz query-de StudentEntity yerine student(table-in adini) yazmisig yeni entity-in adini normalda bu yaxsi deyil
@Entity den name-i silsek StudentEntity clasinda defaul olarag entity-in adi clasin adi olur ve rahatligla gelib
query-de StudentEntity yazirig student yerine.Bes bu zaman hardan bilecek ki men hansi table ile isleyirem  bu zaman
asagi @Table yazib bildiririk-

@Entity
@Table(name="student")
public class StudentEntity {
Belelikle entity-in adi qalir StudentEntity, table-inda student.Table olmayanda entity-e yazilan name hem
entity-ni(jpql-de istifade edesen) hemde table-i(hansi table-a baxirsan) ifade edir.Hemcinin diger entity claslarda
(SchoolAddress, TeacherEntity, SchoolEntity)  da bu cur edirik adina uygun olarag.
@Entity yazilmasi clasin jpa-da registr olmasi ucundur yeni onu repository-de entity gozuyle cagira bilmek ucundur.
CriteriaBuilder- bezen dinamic query-lere ehtiyaca olur yeni ki if filan sey olsa id-ye gore axtar, if fesman sey olsa
age gore axtar ancag goturub onu repository-de bele yaza bilmerik.Bunun evezine criteriabuilder istifade edirik , bu
birbasa claslarla, obyektlerle isleyir ve ona esasen de sql generasiya edir. Bunu istifade etmek ucun Repository
interface-ni hemcinin  extends edirsen JpaSpecificationExecutor-den.Ondan sonra service terefde is gorursen.Numune
olarag spring.io (google da sprign data Criteria Builder yazib) goturub StudentEntity-e yazirig.Hemcinin
baeldung.com-dan da numune goturub bir metod-da bunlari birlesdirib bezi deyisiklikler edirik proyektimize uygun-

    public List<StudentEntity> findByAgeAndName(String name, Integer age){
        return studentRepository.findAll(new Specification<StudentEntity>() {
            public Predicate toPredicate(Root<StudentEntity> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                Root<StudentEntity> student = query.from(StudentEntity.class);
                Predicate namePredicate = cb.equal(student.get("name"), name);
                Predicate agePredicate = cb.equal(student.get("age"), age);
                return cb.and(agePredicate, namePredicate);
            }
        });
    }


Demeli burda deyir ki ilk once bu metoda gore axtarisa ver-
 public List<StudentEntity> findByAgeAndName(String name, Integer age){
sonra studentRepository-nin findAll metodunu cagiririg bu metod JpaSpecificationExecutor-dan gelir bayag extends
etdiyimiz. Bunu extends etmesek findAll-un metodunun Specification tipli parametri olmaycag.Demeli bu metod bizde
Specification obyekti isteyir buda generikdir StudentEntity obyektine xidmet edir.Bu obyekt interface-dir onuncun
quranda implementasiyasini zeruri olarag bizden isteyir.Bizde edirik -
return studentRepository.findAll(new Specification<StudentEntity>() {
            public Predicate toPredicate(Root<StudentEntity> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
implement edende icinde toPredicate metodunu override edir. Bu metod bize 3 deyisen verir root, query, cb. Root hansi
table ile isleyirsen , obyektin hansidi onu ifade edir, query - query-ni execute etmek ucun, cb criteria-ni generasiya
etmek ucun.
Sonra - Root<StudentEntity> student = query.from(StudentEntity.class);
query-e deyirik ki bize root olan bir obyekt ver ki biz student vasitesile islemek isteyirik.Halbuki toPredicate-de
bunu verirdi onuncun yuxarda root deyisenin adini student etsek daha buna ehtiyac qalmir ve edirik sonra  silirik bunu -
(Root<StudentEntity> student = query.from(StudentEntity.class);)
CriteriaBuilder criteria-ni build eden bir obyektdir. Criteria da yeni sertler demekdir.

Predicate namePredicate = cb.equal(student.get("name"), name);
Predicate agePredicate = cb.equal(student.get("age"), age);
return cb.and(agePredicate, namePredicate);

cb-ye deyirik ki equal serti duzelt, yeni student ile Root clasina deyirik ki senin xidmet etdiyin entity ki var ha
onun icinde name adli deyisen olacag, "o name deyiseni eger metodun parametrin de qebul olunan name deyisenine
beraberdise" adli bir  kriteriya duzelt.Student adi bizi casdirmasin deye deyisib root edirik.Burda yoxlama getmir, ne
uzre yoxlayacagsa onun obyekti qurulur.Asagda da eyni age ucun sert duzeldir.Sonra kriteriya builder-e deyirik ki bu iki
serti and ile bir birine yapisdir. Or da yazila bilerdi. ve bu spesifikasiya obyektini bize return edir ve findAll
cagiririg bu spesifikasiya uzre query generasiya olur arxada , bu query ile de neticemizi tapirig.Netice olarag kodumuz
bu halda olur -
public List<StudentEntity> findByAgeAndName(String name, Integer age){
        return studentRepository.findAll(new Specification<StudentEntity>() {
            public Predicate toPredicate(Root<StudentEntity> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                Predicate namePredicate = cb.equal(root.get("name"), name);
                Predicate agePredicate = cb.equal(root.get("age"), age);
                return cb.and(agePredicate, namePredicate);
            }
        });
    }
Bu qeder is bize niye lazimdir? app-imizin database-den asili olmasini istemirikse ve dinamic query duzeltmek
isteyirikse bu bize lazimdir.Meselen deye bilerdik ki -

public List<StudentEntity> findByAgeAndName(String name, Integer age){
        return studentRepository.findAll(new Specification<StudentEntity>() {
            public Predicate toPredicate(Root<StudentEntity> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(name!=null && name.trim().length()>0) {
                    predicates.add(cb.equal(root.get("name"), name));
                }
                if(age!=null && age>0) {
                    predicates.add(cb.equal(root.get("age"), age));
                }
                Predicate[] arr = new Predicate[predicates.size()];
                return cb.and(predicates.toArray(arr));
            }
        });
}
Yen if ile sert qoyardig ki name nulldan ferqlidirse, ve bosluq deyilse yeni trim length sifirdan boyukse predicate-e bu
serti - (cb.equal(root.get("name"), name)); add et, hemcinin age ucunde.Dinamik olarag ad varsa ada gore, yas varsa
yasa gore axtaris ver ve bu sertleri bir birile ve ile birlesdir.Bunu yazmagla dinamik query generasiya etmis oldug.
multiple predicate jpa yazib axtarisa verilmesi and ucun idi yeni nece birlesdirilir.
Bunu bu curde yaza bilerik-
public List<StudentEntity> findByAgeAndName(String name, Integer age){
        Specification<StudentEntity> specification = new Specification<StudentEntity>() {
            public Predicate toPredicate(Root<StudentEntity> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(name!=null && name.trim().length()>0) {
                    predicates.add(cb.equal(root.get("name"), name));
                }

                if(age!=null && age>0) {
                    predicates.add(cb.equal(root.get("age"), age));
                }

                Predicate[] arr = new Predicate[predicates.size()];
                return cb.and(predicates.toArray(arr));
            }
        };
        return studentRepository.findAll(specification);
    }
Ele bilki studentRepository bizden spesifikasiya isteyir onu da new Specification deyirik ve hansi clasa xidmet etiyini
qeyd edirik belece qurulur.Burda olan equal metodunu equals(beraberdirmi?) ile sehv salma. equal yazmagla beraberlik
predicate-ni qurursan , bunun yerine like da yaza bilersen meselcun, basqa lessThanOrEqual var le yazilir yeni ona
beraberdirse ve  ondan kicikdirse demekdir, sonra greaterThanOrEqual, isFalse, not, between ve s.Bunlar hamisi sql-de
olan seylerdir,
bunlardan birin adini cekmekle deyirsen ki mene o query-ni qur.Yeni duzeldib atirib liste bu predicatelerden bir dene
spesifikasiya duzeldenden sonra findAll-a gelib spesifikasiyani ise salanda bu predicate-ler ise dusur.Yeni ele bil ki
ehmed ile memmed var heresine bir is tapsiririg ve deyirik ki indi yox men isare edende ise baslayarsiz ve isare edende
baslayirlar bunun kimi.




If qoymagla filansey bele olanda bu cur olsun deye bilirik normalda query-lerde deye bilmirik. Yuxarda spefication boz
olmagi ondan ireli gelir ki bize lambda teklif edir bizde hele bunu kecmemisik deye deyismirik.
@NamedQuery buda query kimidi demek olar sadece bunu StudentRepository yazmirsan gelib StudentEntity basinda yazirsan
ve ad qoyursan query-e sonra StudentRepository-de adini cekmekle query isini gormus olursan-

@NamedQuery (query="select s from StudentEntity s where s.school.id=:id and s.age=:age", name= "nqfindBySchoolIdAndAge")
public class StudentEntity {

StudentRepository-de yazirsan -
List<StudentEntity> nqfindBySchoolIdAndAge(Integer schoolId, String name);
Ancag kohne texnologiya sayilir cunki bunu bele yazana ele bayag yazdigimiz kimi StudentRepository-de yazmag yaxsidir.
Qeyd olarag deyek ki spring data istifade etmesek, kohne strukturda  namedquery-ni entity manager(em) vasitesile
cagiririg yalniz namedquery-nin adini cekirik. yeni uzun uzadi yazmirig adin cekirik gedir query-ni tapir ve run edir.
Servelet, jsp demek olar islenmir artig. Cox sirketde restful, html istenilir, Testing juniorlardan sorusulmur.
StudentRepository-ni CrudRepository-dende extends ede bilerik JpaRepository-nin evezine , 2 si de eyni yola cixir xirda
ferqleri var meselen, Crud-da sort yoxdu jpa-da var.Bu da artan ve azalan ardiciligla ver demekdir tutalim yasi coxdan
aza isteyirsen.Ancag bunu bele isleden yoxdur bunu da spring data ile StudentRepository-de metod seklinde yazib edirler.
Spring data da 2 dene isi bilmek (metod ile axtaris ve query) bize bes edir demek olar ki.
 */
