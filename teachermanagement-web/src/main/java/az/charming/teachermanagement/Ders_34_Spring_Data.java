/* Database proyektimizde jdbc ile queryler yazirdig, bu dersde bu isleri orm ile goreceyik.Ancag evvelki sekilde
queryler yazmayacagiq, bunu obyektlerle isleyerek hell edeceyik.Build gradle-de dependecy elave edirik.Kohne database
proyektini (jar) sildik.Spring data jpa-ni elave etdik :
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'. Proyekt mysql-e qosulur deye net uzeri axtaris
edib  mysql connect dependency tapdig (maven ucun olaniydi) ve cevirdik gradle formatina :
implementation 'mysql:mysql-connector-java'.Ve kohne proyektden entity(StudentEntity), repository(StudentRepository)
paketlerini goturduk.Importlari duzeldirik xetalari yigmag ucun cunki indi import olan entity, repository paketleri
az.charming.teachermanagement. paketinden goturulur. StudentRepository de arraylist evezine silib list import edirik
cunki controllerde list import olub. Indi bize artig repossitorydeki metodlar( komandalar- delete, insert ve s.) lazim
deyil. Cunki bu imkanlar bize avtomatik verilib.Bununcun StudentRepository-ni interface edirik ve extends edirik
JpaRepository interface-ni :
public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {...
JpaRepository ozunde bu metodlari saxlayir. Ve daxil olsaq JpaRepository-e gorerik generik interfacedir:
public interface JpaRepository<T, ID> bura type-i verirsen ve onun identefkatorunu verirsen bu integer  long falan olar
hemcinin e-mailde identefkator ola biler yeni stringde vere bilerse.. Meselen biz student veririk onunda identefkatoru
id-dir, id-inde tipi integerdir.Bunu yazandan sonra artig bizim insert, delete, update ve s. hamisi var.
JpaRepository-e baxsag extends edir Pagingand... interface-ni oda CrudRepository-ni. Buna daxil olsaq gorerik delete
var, deleteById var, biri obyekt qebul edir onu silir  biri ise id-e gore silir.Ancag bu metodlarin body-si yoxdur.
Bunlar compile zamani override olur, StudentRepository-ni implement eden class qurulur ve hemen metodlarlari override
edir.Biz database-de table-in her sutununa uygun olarag deyisenleri yazirdig StudentEntity-de id, name ve s. Jpa-da,
Orm-de bucur isleyir.Jdbc-de dene dene set edirdik burda avtomatik hamisi set olur.StudentEntity clasinin basinda
@Entity annotasiyasi yazib moterizede (name="student") yazirig ki yeni bu clas student table-ni ifade edir.Sonra id
deyisenin uzerine @Id yazirig buda bizim identifikatorumuz olur.
@Entity(name="student")
public class StudentEntity {
    @Id
    private Integer id;

StudentRepository-de bu entity-e xidmet edir.
Controllerde StudentRepository obyektini(new..) silirik cunki abstract oldu. StudentRepository deyisenin uzerine
@Autowired yazirig onda bayag compile zamani qurulan clasin obyekti proyektimizi run edende spring vasitesile  bu
deyisene menimsedilir:
 @Autowired
    private StudentRepository studentRepository;
Buna deyirler injektor. Yeni StudentRepository-nin obyektini qurursan ve bunu StudentControllere injeksiya edirsen.Bu
proses spring vasitesile arxada gedir.
Findlist xeta kimi gosterir cunki hair metod yoxdu bunu deyiseceyik, add , update metodunda ise insert , update yerine
save yazirig. Cunki framework-de beledir. Eger studentEntity-nin id-si varsa update edir yoxdursa insert edir. Bezi
frameworklerde buna savaOrUpdate de yazilir adi:
 @RequestMapping(value = "students/add", method= {RequestMethod.POST})
    public String add(@ModelAttribute StudentEntity studentEntity){
        studentRepository.save(studentEntity);
        return "redirect:/students";
    }
Delete-de kohnede id gonderirdik burda ise id-ye gore olanda deleteById deyirsen, tekce delete obyekte gore silirdi:
@RequestMapping(value = "students/delete", method= {RequestMethod.POST})
    public String delete(@RequestParam(required = false) Integer id){
        studentRepository.deleteById(id);
        return "redirect:/students";
    }
Hara qosulacagini ise application properties-de yazirig.Url falan jdbc de qosdugumuz adreslerdi ve kohne proyektden
goturub yazirig:
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/librarymanagementsystemdb
spring.datasource.username=root
spring.datasource.password=11111111
Bu rahatciligi bize spring boot verib. Yalniz springde yazsaydig jdbc-deki kimi ayrica clas acib metod qurub
bildirmeliydik.
StudentRepository-ni Jpa-ni extends edib bize hazir xidmetler etmesini spring data teskil edir.
Orm- object relational mapping acilisi beledir. Yeni database-i obyektlerle ifade edecekesen, programist query uzu
gormeyecek.Table uzu gormur yalnizca entity-de qeyd edir ki bu clas bu table-a baxir.Mapping sozu buna goredir yeni
table filan sutunu filan field uygun gelsin.Orm bu bir anlayisdir, bir spesifikasiyadir ve bu butun obyekt yonumlu
proqramlasdirma dillerine samil olunur.
Jpa- java persistence api. Jpa da bir spesifikasiyadir, Yalniz orm-in java ucun olan spesifikasiyasidir.
Orm deyirki table-in filan sutunu filan field-e uygun gelsin, query yazmadan bazaya muraciet etsin, melumati ceke
bilsin ve avtomatik obyekte doldursun.Bu genel bir qaydadir. Jpa deyirki orm-i javada tetebiq ede bilmek ucun filan
annotasiya olsun, filan interface implementasiya olsun, her hansi annotasiyani isledende onu taniya bilsin ve s. Basqa
bir misal: Emin bir framework duzeldir ozunden tutalim EminJpa framework bu duzeltmek ucun jpa spesifikasiyasini tetbiq
etmek lazimdir.
import javax.persistence.Entity;
import javax.persistence.Id;
Burda entity ve id annotasiyasi jpa-nindir.Bunlar javax persistence (jpa kitabxanasinindir)  folderinden gelir. Jpa
adli kitabxana yoxdur , jpa java enterprise edition-nin icinde gelir onu canlandiran diger sirketlerin kitabxanalaridir.
Bunlardan mehsurlari hibernate(haybrneyt interview diqqet edilir duzgun teleffuz et) , eclipselink.
Persistence- datani saxlamaq, bezi yerlerde meselen hibernate-de insert evezi persistent istifade edilir. Spring
default olarag hibernate istifade edir.
public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
Yuxardaki hisse spring verdiyi imkandir ancag arxada istifade etdiyi meselen bazaya insert elemek bu artig hibernate
ise dusur.
Muellimin githubdaki proyektine baxag: EntityManager obyekti var ve uzerinde @PersistenceContext annotasiyasi.Bunlar
javax persistence folderinden gelir.Metod duzeldilib entityManager-in createquerysi cagrilir sql-e muraciet edilir ve
deyilir ki sql-den cavab qaytaranda CsChargeTariff clasina doldur qaytar.Bu misal spring olmasa sirf jpa-nin verdiyi
imkan ucun gosterildi.Yeni entity manager acib adi uzerinde entityleri idare edirsen (insert, delete ve s.). Neceki
jdbc-de connection , driver, preparestatement variydi bunlar jdbc spesifikasiyalari idi, jdbc-ni implement eden MySQl,
Oracle ve s. variydi, heresininde oz driveri variydi ancag hamisi jdbc interfacelerini implement edib, eyni qaydada
jpa-da da beledir.Jpa-in oz annotasiyalari var , oz interfaceleri var bunlari  da implement edibler bayag dediyimiz
hibernate, eclipselink.
Spring data da ise bunlari el ile yazmirig cunki arxada ozu edir, hetta default olarag delete, select  ve s.
funksiyalarini avtomatik verir.Spring datanin elave ustunlukleride var. Meselen StudentRepository-de bele bir metod:
List<StudentEntity> findByNameOrSurnameOrAgeOrScholarship(String name, String surname, Integer age, BigDecimal
scholarship);
yazmagla bu funksiya avtomatik iceride implementasiya olacag, ad, soyad queryleri bura oturacag ve nehayet bunu
yazmagla ada, yasa, soyada ve s. gore avtomatik axtaris vermis oluruq.Spring data daha cox ormdir neinki jpa.Daha cox
obyekt yonumludur.Metodun adinda sutunlar falan olmali deyil cunki biz ancag obyketlerle isleyirik(class, deyisen
falan olmali), query dusunmemeliyik.Parametrdeki deyisen adiyla uste uste dusmeyi vacib deyil], yeri duzgun gelsin yeni
ilk name demisik  ve ilk name parametri gelir.StudentControllerde:
List<StudentEntity> list= studentService.findByNameOrSurnameOrAgeOrScholarship(
                name,
                surname,
                age,
                scholarship
        ); yazirig ve run edirik browerserde default olarag cedveldeki sexslerler gorsenmir yalniz ad,soyad ve s. yazib
        axtaranda hemen sexs cixir. cunki findByNameOrSurnameOrAgeOrScholarship buna gore axtaririg.Bunun qarsisini
        almag ucun bir service paketi ve icerisinde StudentService clasi acirig. clasin uzerine service  oldugunu
        bildirmek ucun @Service yazirig- @Service
public class StudentService { , sonra istifade edeceyimiz repository-ni yazirig -
@Autowired
private final StudentRepository studentRepository;
Sonra bu iki metodu yazirig:
public List<StudentEntity> findAll(String name, String surname, Integer age,
                                       BigDecimal scholarship){
        if(isAllEmpty(name, surname, age, scholarship))
            return studentRepository.findAll();
        return studentRepository.findByNameOrSurnameOrAgeOrScholarship(name, surname, age, scholarship);
    }

    private static boolean isAllEmpty(Object... objs){
        for(Object obj: objs){
            if(obj!=null) System.out.println("obj="+obj.toString());
            if(obj!=null && !obj.toString().isEmpty()) return false;
        }
        return true;
    }
Demeli burda (isAllEmpty-de) obyekt massivi qururug ve for eachle bir bir yoxlayib obyekt nulldan ferqlidirse demek
nese var , adam neyise axtarisa vermek isteyir.eger empty olsa findAll yeni butun datalari versin.
if(obj!=null && !obj.toString().isEmpty()) return false; - burda yoxlayir obyekt nulldan ferqlidirse stringe cevirib
baxir empty deyilse cixis versin.Yeni web-de search-de bos bir axtarisda etsen findAll verib datalari gostersin bu
olmayanda bos axtarisda siyahi cixmir.Cunki biz stirnge cevirib empty olmagini yoxlamasag, bos verende "" bele bir
string verir bu nulldan ferqlidir ancag empty-dir bele halda isAllEmpty false qaytarir ve findAll metoduna dusmur -
if(isAllEmpty(name, surname, age, scholarship))
            return studentRepository.findAll();
Hetta bunu yazmagla null dan ferqli olanda string seklini de yoxlayirig capa vererek-
 if(obj!=null) System.out.println("obj="+obj.toString());

StudentService-in clasin basinda @Service yazirig ve eslinde StudentRepository clasin uzerinde @Repository olur ancag
yazmayanda da problem elemir cunki biz extends JpaRepository etmisik buna esasen taniyir ki bu bir repository-dir.
Repository-nin isi odurki database ile unsiyyetde olacag ( insert, update ve s. edecek).
Service-in isi ise odurki service repository-ni islederek bazaya muraciet edir lakin service bezi sertler qoya bilir,
filan sey olsa bele olsun kimi.(if-le falan bayag yazdigimiz kimi).Repository birbasa database ile elaqe saxlayan,
service ise cerez cerez bezi isleri goren lazim geldikde repository-ni isleden clasdir.Service Repository istifade
etmeye de biler meselen ele bir service duzeltmisen ona list gondermisen muhasibatlig isi gorur, ordan ortalama pulu
cixardir ve s. tipli yazmag olar.Eslinde bu annotasiyalar vacib deyil, bunlarin koku gedir cixir @Component-e.Yeniki
springde @component-i ona gore yazirig ki istediyimiz vaxt  clasin obyektini qura bilek (StudentService bunun kimi).
Controllerde uzerine @Autowired yazib avtomatik qurmusug cunki basinda @Service var. @Service -e daxil olanda goreceyik
ki @Component var -

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {
@component yaza bilerikse @service falan niye yazirig? bu claslari ayird ede bilmemiz ucundur adindan ayird etmek olur
ancag framwork yazanda fikirlesibler gelecekde bir plan olar nese yazila biler ki bu ancag meselen uzerinde @service
olan claslara aid olsun  ve s kimi.
@Autowired tercih olunmur normalda. Esasen 4 cur injeksiya var autodetect(hazirda istifade olunmur), contructor, tipe
gore, ada gore.
meselen StudentService clasinda -
@Autowired
    private StudentRepository studentRepository;
tipe gore baxir ve obyekti implementasiya edir.Iki dene bu interface implement eden clasimiz olsaydi o zaman yazsaydig
bilmeyeceydi autowired  hansini etsin, conflict vereceydi.Meselen service-de bir interface acirig StudentServiceInter
adli ve 2 ci bir clasda acirig StudentService2 . Her iki clas (StudentService, StudentService2) bu interface implement
edir ve biz controllerde gelib -
 @Autowired
    private StudentServiceInter studentService;  yazanda konfilikt verecek, ancag tekce StudentService clasi  olsaydi-
 @Autowired
    private StudentServiceInter studentService = new StudentService();
bu clasin obyektini qurub  interface injekt edeceydi.Konfiliktin qarsini almag ucun uzerinde @Qualifier yazirig-
@Autowired
@Qualifier("studentService2")
    private StudentServiceInter studentService;  bes bu burda niye student balaca yazildi cunki basinda @Component olan
her bir clasin adina esasen, java konpensasiyasina esasen onun obyekti qurulur bu razilasma da ilk herf balaca yazilir.
Bele halda her iki clasin obyekti qurulur hazir gozduyur, @Qualifier-de hansini yazsan mehz onu goturur injekt edir.Biz
clasin uzerinde olan service icinde adda qoya bilerik ki qualifier da bunla cagirag hemen clasin obyektini tutalim -
@Service("alma")
@Qualifier("alma") yazib cagiririg ancag default olarag service-de hecne yazmayanda clasin adina uygun olarag (balaca
herfle baslayan) ad qoyur.
@Autowired edende tipe gore edir. Birde constructora gore edirsen-
@Controller
public class StudentController {
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    public StudentController(StudentRepository studentRepository, StudentService studentService) {
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }
Autowired sildik ve cons-u qurduq ve final yazmagi meslehet gorurki bir defe bura obyekt menimsetdinse artig asagida
ona basqa obyekt menimsetme kodu qirir .Final yazmagla yeni qaranti etmis olursan.Bu hal beyenilen haldir ona gore ki
meselen autowired olanda  biz contorllerin obyektini qurub index metodu cagirmagla test etmek istesek onda
setStudentRepository deye bilmerik ve index metodunu cagiranda studentService partlayacag ki NullPointerException.
Ancag constuructor olanda yalanda 2 dene studentRepository obyekti duzeldib controllerin konstruktoruna oture bilersen
meselen bele- new StudentController (new StudentRepository(), newStudentService()); gorunduyu kimi bele cagiranda gedir
oturur konsdaki deyisenlere ve index metodunu cagiranda da cagira bilirik.
Testler bele yazilir ayrica clas acilir ve hemen clasin icinde bu is gorulur-
new StudentController (new StudentRepository(), newStudentService());
bunlar yazilir oturur konsa ve controllerin metodlari cagirilarag test olunur.Onuncun servicede de kons vasitesile edin-
@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
Hacansa service-i test etmek isteyende icine repository-de atmalayig ki findAll metodunda repository cagiranda
NullPointerException partdamasin.
Jpa-nin spesifikasiyasina baxag, bir var yanasma - burda meslehet kimi olur, biri de var spesifikasiya burda konkret
deyir bu cur olmalidi,oracle-da (jpa specification documantion yazdig google da) java persistence api baxsag bu
spesifikasiya da persistence unit deyilen sey var - yeni burda normalda entitylerin hamisini yazmalisan ki menim bele
bele entitylerim var, bu da spring data olmayan haldadir, spring data goturur bizim avtomatik entitylerimizi hamisini
persistence unit-e elave edir ve bunu istifade edir amma yene de spesifikasiyada var.

Burda criteria api var- yeni criteria yaziriq evvelki dersde dinamik query yazmisdig ki if name gelse bunu qoy, surname
olsa onu qoy  ve s. bu tipli seyler yaza bilirik burda.Yene de query uzu gormeden obyektlerle isleyirik ele bil ki,
kriteriyalar duzeldirik.Burda cox movzu var lazim olarsa baxmag olar gelecekde.
Persistence xml anlayisi spring datada yoxdur, misal olarag muellim kohne app-inden numune gosterir hansi ki daha once
baxmisdig ( CsChargeTariff), hansi ki orda gostermisdik  database haqda, entityler haqda nelerin oldugunu ve yuxarda da
<provider> icinde PersistenceProvider olarag eclipse istifade etdiyimizi qeyd edirik.Bu jpa-nin qoydugu qaydadi-
hibernate-de , eclipse-de bunu destekleyir ,butun bunlari xml spesifikasiyasi teleb edir qisasi.Spring datada buna
ehtiyac olmur, ozu bunlari arxada edir bizimcun (jpa-ya deyirki bizim enttiyler bunlardi, hibernate-e deyir url-ler
falan bunlardi ve s.)
Dao- repositorynin diger adidir, aciglamasi data access object demekdir, repositoryde beleydi bazadan datani alib
insert edirdi, bu bir patterndir,muellimin numunesine daxil olub baxa bilerik (impl folderinde).Burda flush var bu o
demekdir ki proses baslayanda evvelce insert gedir sonra commit olur eger database-in ohdesine buraxsan database
commiti sonra da ede biler ancag flush deyende onu avtomatik commit etmis olursan yeni tesdiqleyirsen ki bazaya otursun.
Adeten hibernate istifade olunur, eclipselink jpa-nin default implementasiyasidir, yeni jpa-nin spefisikasiyasindan
qiraga cixmir yeni ne imkanlari varsa odur, ancag hibernate daha gucludu elave imkanlari var.Neceki spring data
spefisikasiyadan elave isler gorurdu, imkanlar qururdu bunun kimi.Dunyada da hibernate istifade olunur (Spring de
default olarag bunu istifade edir) ve daha suretli , daha guclu hesab olunur.
Hibernate-in caching imkani var- yeni sen bazaya muraciet edirsen bir nece defe bezen oxsar ve ya eyni datani
goturursen bu zaman hibernate evvel goturduyu eyni datani caching-de saxlayir ve lazim oldugda burdan goturur bir de
bazaya muraciet etmir.
Getdikce inkisaf gedir ve caching ucun artig bir o qeder de hibernate teriflenmir, bir nece caching var istifade olunan
bir hazelcastdir .Bu distributed caching edir, Distributed cach nedir? microservice cixandan sonra yanasmalar deyisdi
ve caching sistemide deyisdi, evveller monolit application istifade olunur yeni yalniz bir application ayaga qalxa
bilirdi buna gorede hibernate cachi istifade olunurdu, sonra distributed sistemler cixdi yeni
StudentTeacherManagementSystem appi qurursan bununcun bir dene StudentManagementSystem appi qurursan, bir dene
TeacherManagementSystem appi qurursan lazim oldugca 2 si ne de muraciet edirsen ve bunlar bir biriyle elaqede olur,
Bu distributeddir (yeni parcalanmis demekdir). Bu zaman hazelcast istifade olunur ancag Azerbaycanda istifade eden
gormeyib muellim)).
Workbenche daxil olurug evvelden qurudugumuz, datalaramizi saxladigimiz librarymanagementsystemdb-de bir
school adli table qururug(onceden student table-imiz variydi) - id(int; pk, nn, ai) ve name(varchar(45)) adli
column-lar qurdug.Sonra student table-na gelib orda evvelden  olan name, surname,
scholarship column-larin altina bir column da yazirig - schoo_id(int).Asagida foreign key hissesine daxil olub foreign
key name yerine fk_school_id yazirig, referenced table-da "librarymanagementsystemdb school" secirik ve column -da
school_id isare edib id secirik apply edirik.Yeni student-in(icinde oldugumuz table-in) school_id sutunu school-un
(referans etidiyimiz table-in) id-sine baxsin.Yeni student table-inda foreign key qururug school table ucun. Bunu apply
edende mende problem oldu query-ni hazirlamadi ona gore de men bunu el ile yazib run etdim -
ALTER TABLE `librarymanagementsystemdb`.`student`
ADD CONSTRAINT `fk_school_id`
  FOREIGN KEY (school_id)
  REFERENCES `librarymanagementsystemdb`.`school` (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
Bunu apply edende ozu teqdim etdi ancag morterize icleri bos idi ona gore secdiyim sutunu yazib run etdim ve oturdu.
Ve gelib school-da id -name sutunlarina 2 dene setr elave edirik-
id      name
1	6 nomreli mekteb
2	5 nomreli mekteb
Sonra student-e gelib siyahida olan sagirdlerin shcool_id -sine 1 yazirig yeni 1 id-li mektebde oxuyurlar.Biz bura qeyd
ede bileceyimiz halda niye foreign key qurdug ona gore ki burda real datalar olsun istedik yeni biz student-de
school_id-ye 3 vere bilmerik cunki 3 id-li mekteb yoxdu.
Relationshipler-  Bir dene teacher table-i
qurdug ve 2 sutun yazdig id(int; pk, nn, ai) ve name(varchar(45)). Bir dene de school_student adli table qurdug -
id(int; pk, nn, ai),
student_id(int) ve teacher_id(int) sutunlu.Sonra bu table-in(school_student) foreign key-sinde 2 dene fk qururug.
fk_student_school - librarymanagementsystemdb '.' student - student_id id;
fk_teacher_school - librarymanagementsystemdb '.' teacher - teacher_id id;
School_student cedvelinde yazirig -
id     student_id      teacher_id
1	7	              1
2	7	              2
3	12                1
Yeni fk da qeyd etdik school_student table-in student_id-si baxir student table-nin id-sine(hemcin teacher ucunde etdik)
ve 7 idli telebenin muellimi 1 id li ve 2 idli muellimlerdi, 12 idli telebenin ise muellimi 1 idli muellimdi.Burda
baxanda goruruk ki bir telebenin bir nece muellimi olar hemcinin bir muelliminde bir nece telebesi- bu many to many-dir.
Tebii ki teacher table-da bu muellimleri yazirig-
id     name
1	   Sarkhan
2	   Ilqar
Bir nece telebe bir mektebe baxir buna deyirler many to one elaqesi, bir mekteb bir nece telebeye baxir buna ise
deyirler one to many elaqesi, eger her telebeye uygun bir mekteb yazilsaydi studentin studentin school_id-sinde yeni
meselen yazdigimiz bir bir kimi yox, 1, 2, 3 seklinde bu one to one olardi.Meseleye mentiqi baxilir.Bu elaqeler
table-lar arasindadir. Bir school_address adli table qururug - id (int; pk, nn, ai), name(varchar(45)),
school_id(varchar(45)) ve cedvelinde yazirig-
id  name   school_id
1	Baki	1
2	Sheki	2
burda bir mektebin bir adresi olmalidir mentiqi olarag, bu one to one elaqesidir.Ancag texniki olaraq iki adrese eyni
mekteb id-si yazmag olar.
Bir exam_result adli table qururug- id (int; pk, nn, ai), result (varchar(45)), student_id(int) ve foreign key-sinde
bir fk yazirig- fk_exam - `librarymanagementsystemdb`.`student` - student_id id.
Cedvelinde yazirig-
id  result  student_id
1	100	     7
2	30	     7
3	40	     12
4	100	     12
burda bir telebenin bir nece resulti var (fenne gore ola bilir). exam_ result ile student arasinda many to one-dir,
student ile exam_result arasinda one to many-dir.
school_student-in adi yalnis olub, deyisib student_teacher edirik (novbeti dersde bunun davami olarag bildirilir).
*/
