package org.lasseufpa.circular;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    boolean busOn = false;                    //flag para inicalização
    private final int timeWait = 200;         //tempo de atualização em milisegundos
    private final int NCircularPoints = 439;  //numero de pontos da rota do circular
    private final int NStopPoints = 5;        //número de pontos de parada

    //mensagens gráficas
    private TextView status;
    private TextView viewMessage;

    //handler para capturar mensagens
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
               //PublishMessage(msg.getData().getString("message"));
                viewMessage.setText(msg.getData().getString("message"));
            }



        }
    };

    //repositório de circulares no mapa
    public static final Respositorio repositorioCirculares = new Respositorio();

    //objeto de comunicação MQTT
    private  MqttConnect mqttconnect;

    public static final double[] rotaX = { -48.45721595321921, -48.45668234322626, -48.45629487925789, -48.45601133799832, -48.45587919692488, -48.45585351733713, -48.45581573454331, -48.45576992402571, -48.45572290620589, -48.45573265613829, -48.45569219657421, -48.45566730006063, -48.45563243048813, -48.45559224605859, -48.45558462004932, -48.45552022559654, -48.45532106072942, -48.4550825063755 , -48.45481717186889, -48.4546529137942 , -48.45454819405587, -48.4543608491854 , -48.45418983982678, -48.45397810097917, -48.45388718114886, -48.45398843114077, -48.45410874302211, -48.45424411801952, -48.45433675955631, -48.45440781135319, -48.45451017152761, -48.45457436501346, -48.45456192180846, -48.45441733124209, -48.4541868040027 , -48.45390301357543, -48.45350757888333, -48.45322093813007, -48.45295827875416, -48.45260676819169, -48.45230600040463, -48.45207030510525, -48.45177576020107, -48.45149959838938, -48.45126382565425, -48.45096440990883, -48.45078595472724, -48.45047646181827, -48.45022496771506, -48.44989948502897, -48.44951638109562, -48.44925195300259, -48.44905327660958, -48.448914118048 , -48.44881812607965, -48.44879167873587, -48.44865889160113, -48.4483657669868 , -48.44808215751214, -48.44795136041581, -48.44792541253506, -48.44795796815734, -48.44799137293959, -48.44801940869424, -48.44799849115374, -48.44798028237929, -48.44793489444774, -48.44787868706409, -48.44784883718106, -48.44780913275352, -48.44776293574169, -48.44764372157897, -48.44742183648742, -48.44718667379775, -48.4469651092983 , -48.4467545665457 , -48.44652119374413, -48.44630394351704, -48.44621892247509, -48.44629361769229, -48.44644870568096, -48.44658060501956, -48.44667949241073, -48.44666588812893, -48.44674997214628, -48.44693968135201, -48.44707355774841, -48.44726824606121, -48.44747404306828, -48.44764797313874, -48.44775610889346, -48.44787336024857, -48.44795871374627, -48.44801421773038, -48.44805039097276, -48.44808341912068, -48.44810469634388, -48.44814150996659, -48.44815280176091, -48.44816888745699, -48.44818800733891, -48.44815268024817, -48.44811706750735, -48.44804848252257, -48.4479690093764 , -48.44783403317546, -48.44769281748015, -48.44758446113612, -48.44748202982514, -48.44742035099643, -48.44730351932458, -48.44716126204292, -48.44700841991799, -48.44691736410717, -48.44683816268754, -48.4466937896025 , -48.44657075461539, -48.44646833472255, -48.44631952408864, -48.44619290661861, -48.44604669948659, -48.44591883955358, -48.44576877976402, -48.44560529092181, -48.44551786163408, -48.44542310492771, -48.44532602152577, -48.44523681684939, -48.44515517599084, -48.44507178138269, -48.4449618998968 , -48.44487273296559, -48.44478242071917, -48.44470202557061, -48.44464161691135, -48.44458224356537, -48.44455922414895, -48.44461630438067, -48.44458809195577, -48.44457346792758, -48.44454998544223, -48.44454745122379, -48.44453418488821, -48.44454243132404, -48.44456371389169, -48.44457658431631, -48.44461265033432, -48.44462068158077, -48.44464705890097, -48.44465987712223, -48.44467258863027, -48.4446781933264 , -48.44462644316035, -48.44461195631494, -48.44455616464422, -48.44451921920955, -48.44447004821517, -48.44440558980476, -48.44438683285926, -48.44437047896567, -48.44429483379581, -48.44423545941326, -48.44413317589147, -48.44403487743579, -48.44394854158266, -48.44382800644746, -48.44366306502482, -48.44355067028241, -48.44346288309538, -48.44333834455323, -48.4432082834778 , -48.4430380120859 , -48.44294588435842, -48.44279586767567, -48.44260723047176, -48.44250255542077, -48.44237210807774, -48.44225451333979, -48.44211890417215, -48.44202797139695, -48.44194154718782, -48.44184163359403, -48.44173590373144, -48.44164428645673, -48.44155420371538, -48.44147905656661, -48.4414078436977 , -48.4413758150581 , -48.44129854906711, -48.44119275059153, -48.44116660806974, -48.44122595247749, -48.44135438442574, -48.44145072660498, -48.44154314050498, -48.4416580007603 , -48.44174202708874, -48.44182354723895, -48.44192941382734, -48.44204526461111, -48.44216727854543, -48.44224825268375, -48.44235398236776, -48.44242935358789, -48.44252083071023, -48.44266236882675, -48.4427700412821 , -48.44292815632091, -48.44306628080894, -48.44318455341062, -48.44329122521845, -48.44337420776878, -48.44347160114276, -48.443556503625 , -48.44361259150482, -48.44374885619011, -48.44381199745848, -48.4438977202636 , -48.44398073074221, -48.44408372786101, -48.44415925565057, -48.44422431436114, -48.44427794507208, -48.44435355140716, -48.44443418060589, -48.44449298821827, -48.44453489169419, -48.4446301509503 , -48.44465142691872, -48.44464152067822, -48.44470255408289, -48.444734746649  , -48.44474366050061, -48.4447718691687 , -48.44477181465545, -48.44475753669515, -48.44476464676181, -48.44474536609939, -48.44473714877628, -48.44471340983107, -48.44469602482707, -48.44467144111722, -48.44465480441365, -48.44465295748014, -48.44465549611323, -48.44467062840196, -48.44467932501144, -48.44468926306103, -48.44472174405677, -48.44471964118858, -48.44474781391552, -48.44479538976977, -48.44485368809953, -48.4449154282793 , -48.44498556325301, -48.44511799366151, -48.44520334370572, -48.44528251326244, -48.44539611979901, -48.44549231292255, -48.44558173184732, -48.44566277275972, -48.44578268709719, -48.44591225398488, -48.44604978590144, -48.4462217560834 , -48.44642677569232, -48.44655492374297, -48.4466806469779 , -48.44683211647288, -48.44695976424969, -48.44702969702036, -48.44714339831472, -48.44727380635483, -48.44740289575032, -48.44752853456146, -48.44759169941439, -48.44768028541486, -48.44778135176382, -48.44793829613368, -48.44806367677222, -48.44810704240329, -48.44817531317408, -48.44823518515894, -48.44823401432535, -48.44820995196622, -48.44820018849183, -48.44818913794499, -48.44816874321879, -48.44817981729971, -48.44814908521604, -48.44813442502446, -48.44812532215471, -48.44808735297246, -48.44806958593748, -48.4480350688892 , -48.44800901625543, -48.44794131916426, -48.44788483633824, -48.44784391798201, -48.44779341910152, -48.44782267315954, -48.44785392857979, -48.44789726995315, -48.44792815309062, -48.44795259608838, -48.44796763298374, -48.44798496496473, -48.44800824925822, -48.44802502386969, -48.44804189749922, -48.44805255108765, -48.44804377297825, -48.44801862754817, -48.44796921033861, -48.44795235682601, -48.44802496021646, -48.44822544313085, -48.44849310755767, -48.44865341831374, -48.44875178258862, -48.44876002505136, -48.44881469155067, -48.44888560534802, -48.4489621527861 , -48.44912065220742, -48.44925408330697, -48.44939410071041, -48.44958344096757, -48.44981955643377, -48.4500215472742 , -48.45017746219616, -48.45032063067134, -48.45047679020202, -48.45059723391388, -48.45076550778496, -48.45087524996049, -48.45099523774641, -48.45109765118111, -48.45126507557963, -48.45142667851845, -48.45163754103781, -48.45184299248954, -48.45204412714586, -48.45218689987409, -48.45242976839888, -48.45266692409253, -48.45284251235304, -48.45308206454705, -48.4533667639172 , -48.45352902010146, -48.45373167138948, -48.45397970532392, -48.45414868414946, -48.45432403520311, -48.45446300626291, -48.45452122131363, -48.45454245030635, -48.45452310761531, -48.45448903807576, -48.45443190389791, -48.45438056128381, -48.45435407163846, -48.45428999768623, -48.45421243442802, -48.45413666428682, -48.45407271489943, -48.45400936982926, -48.45394757365736, -48.45385443564289, -48.45382444847687, -48.45391732777129, -48.45407446097501, -48.45418156230216, -48.45431279062179, -48.45443103451221, -48.45451317336165, -48.4546163281746 , -48.45479632363727, -48.45497767819673, -48.45508788184551, -48.45523215087973, -48.45536624246625, -48.45553731896963, -48.4556767170983 , -48.45578401748812, -48.45593618629592, -48.45608819460272, -48.45623418897161, -48.45638423213443, -48.4565191784115 , -48.45669713769854, -48.45685464403948, -48.45702265254072, -48.45717981217302, -48.45737727932742, -48.45754429066304, -48.45765098006017, -48.45775251272472, -48.45786756975441, -48.45796060616679, -48.45801072921109, -48.45807205612866, -48.45814046512317, -48.4581950641576 , -48.45825231604066, -48.4583051741238 , -48.45834322955583, -48.45838376142731, -48.4583320101935 , -48.45829003878952, -48.45824328533704, -48.45826743628587, -48.45827062245284, -48.45830358659368, -48.45833962932428, -48.4583708837873 , -48.45840505858577, -48.45843458563991, -48.45843596236313, -48.4584728570381 , -48.45846664781794, -48.45850484451412, -48.45854987917585, -48.45857490321019, -48.45858783817457, -48.45857456931888, -48.45855354787163, -48.45849882770565, -48.45849407806431, -48.45846055000548, -48.45840953884347, -48.45832710308546, -48.45822643613228, -48.45807839233058, -48.45782751613857, -48.45761859333526, -48.45744196537461, -48.45731277153534};
    public static final double[] rotaY = { -1.472465599146933, -1.472567062439942, -1.472704964252519, -1.472783616693241, -1.473175009422363, -1.47348129064598, -1.473801658243677, -1.474107483796194, -1.474421242974581, -1.4747616278385, -1.475100805420439, -1.475470193789666, -1.475835646114005, -1.476167665623711, -1.476482101472759, -1.476776445266772, -1.476844884409034, -1.47671281158054, -1.476557405970045, -1.476455492653672, -1.47635219578199, -1.476197523142925, -1.476064623339539, -1.475935485505809, -1.475714438597709, -1.475469832623386, -1.475236029604095, -1.474972465295155, -1.474731380232726, -1.474507134264862, -1.474204183562051, -1.473883145149523, -1.473673712092441, -1.473526886952868, -1.473396487224411, -1.473352796674586, -1.473377616035751, -1.473355635928234, -1.473306115009176, -1.473306310980246, -1.473230945580992, -1.473134440384332, -1.47299697415644, -1.47287235405816, -1.472740217419825, -1.472489771668026, -1.472242549711779, -1.471889024002707, -1.471685822053771, -1.471576209576596, -1.471662630756298, -1.471847499249429, -1.472063225421381, -1.472299131963476, -1.47260902221434, -1.472868427027919, -1.473076923729042, -1.473070938160626, -1.472958820432294, -1.472848506039093, -1.472609411610475, -1.472337487476073, -1.472063104117564, -1.471833002539117, -1.471544593571001, -1.471299970986497, -1.471037725818354, -1.470866017752922, -1.470622250351667, -1.470457312122213, -1.470257757555101, -1.470183779974734, -1.470255917590239, -1.470272249554979, -1.470248022953375, -1.470125271346799, -1.470042489692337, -1.469943056599678, -1.469887626427026, -1.469734659550057, -1.469679822879062, -1.469728013006605, -1.469831758683457, -1.469985604362284, -1.470064369808261, -1.470189745104118, -1.470236621621243, -1.470270023439739, -1.470225117296965, -1.470127109456564, -1.47004080599285, -1.469918794128738, -1.469800503744966, -1.469654593624851, -1.469485615000944, -1.469323056357205, -1.469093714507099, -1.46890341339283, -1.46870564085018, -1.468473550284067, -1.468300084681786, -1.468147241200419, -1.468024786638428, -1.467904473200924, -1.467817780735703, -1.467703033445082, -1.467648670838549, -1.467552013049414, -1.467492724491767, -1.467417335514694, -1.467406519122541, -1.467379750525803, -1.467343182575675, -1.46726330616962, -1.467222830854764, -1.467176429817046, -1.467134644357027, -1.467103761985599, -1.467050055678047, -1.46701952952986, -1.466965982644248, -1.466914960730942, -1.466876704181586, -1.466980609630335, -1.467078519014807, -1.467181648445702, -1.467282731566454, -1.467360721943405, -1.46744637504122, -1.467523149798293, -1.467580478031526, -1.467589531435677, -1.467540485577107, -1.467415344160587, -1.467306692328017, -1.467182336368169, -1.467103394466424, -1.466999393662743, -1.466834792867463, -1.46668076203788, -1.466483788909792, -1.466314037224354, -1.466145707727643, -1.466012563413717, -1.465859214062976, -1.465674998414668, -1.465483193961133, -1.465300596275295, -1.465079654405526, -1.464900656956662, -1.464724384370586, -1.464479483739741, -1.464291264757624, -1.464065875721384, -1.463910328335851, -1.463735908635674, -1.463599731499981, -1.46350338726383, -1.463368064807281, -1.463193919487908, -1.463040821629152, -1.462887407766339, -1.462692336927682, -1.462532559094429, -1.462375768861589, -1.462216377689139, -1.462036885088797, -1.46189420673855, -1.461789981328112, -1.461683190154188, -1.461539373943713, -1.461427616201576, -1.461311454955207, -1.461190914112759, -1.461061853023542, -1.460974481355277, -1.46109827743645, -1.461262113235021, -1.46139401992428, -1.461515171180961, -1.461658226766336, -1.461763429255335, -1.461920685685645, -1.462012690889672, -1.462155331274917, -1.462201681180908, -1.462346799486988, -1.462494622813361, -1.462592199100241, -1.462577348328266, -1.462457105592455, -1.462380837630825, -1.46231357664219, -1.462171561712849, -1.462047934996471, -1.461919549048911, -1.461815719718954, -1.46169994325041, -1.461575252511095, -1.461415065184373, -1.461274217138886, -1.461164297027292, -1.461058227106875, -1.460945010407105, -1.46085448542556, -1.460896472318253, -1.460981186035142, -1.461100484536439, -1.461216901637388, -1.461314394287574, -1.46142329549667, -1.461494177308886, -1.461580667352826, -1.461684460799391, -1.461743934583256, -1.461871220418609, -1.461977886233205, -1.46206571066181, -1.462192634248833, -1.462313526579072, -1.462440631105449, -1.46256572583317, -1.462698776350989, -1.462824630923995, -1.462987810198262, -1.463121739921825, -1.463263629581094, -1.463361011359003, -1.463463080083279, -1.463633228607589, -1.46385560503954, -1.463990361678901, -1.464187333354526, -1.464347936557335, -1.464488603120645, -1.464653175442602, -1.464815416944675, -1.465015211716479, -1.465174886981748, -1.465368354089369, -1.465557747081479, -1.465725303031007, -1.46590958493353, -1.466065173298749, -1.466240972321426, -1.466388284120497, -1.46654026575654, -1.466682050906747, -1.466826608356597, -1.466956213087599, -1.467095835951767, -1.467249285368365, -1.467404997690201, -1.467529812201609, -1.467513718553366, -1.467422654571869, -1.467330165050175, -1.467242189399093, -1.467133135310671, -1.467026106905933, -1.46693497071399, -1.466871979280447, -1.466827289373038, -1.466854657016191, -1.46691231277519, -1.466962536529734, -1.46704255866932, -1.467078006740951, -1.467129728881917, -1.467176794861726, -1.467245363202385, -1.467309722710735, -1.467334603733329, -1.467361933818253, -1.467372356523645, -1.467407389851596, -1.467491989495326, -1.467551047263229, -1.467616248166767, -1.46771088779105, -1.467817893447046, -1.467925307556851, -1.468065931159275, -1.468200880010851, -1.468388009740435, -1.468504716990226, -1.468626050362067, -1.468764095261348, -1.468882556005405, -1.469004849454623, -1.469141345678736, -1.469272505287682, -1.469401097590595, -1.46952435186327, -1.469649408061087, -1.469730522171387, -1.469820353203271, -1.469929165312797, -1.469992665690833, -1.470047648870326, -1.470183442816297, -1.470305179936165, -1.470450999357342, -1.470592692075035, -1.470729557394315, -1.470864143975023, -1.470971142624303, -1.471085293101011, -1.471221369459282, -1.471387426290124, -1.47157676623773, -1.471758713063207, -1.471921109252969, -1.472154139223602, -1.472442922806079, -1.47271482278213, -1.472892776361281, -1.472993270116884, -1.473043451179959, -1.472988754640769, -1.472805875819768, -1.472647907316598, -1.472440484676767, -1.472253939619458, -1.472090616609078, -1.47186883668075, -1.471752272512632, -1.471669344384424, -1.47157839748583, -1.471523432493226, -1.47155219585076, -1.471593160279772, -1.471688490393534, -1.471811872073276, -1.471944670543436, -1.472113359829437, -1.472261262117014, -1.47241356318653, -1.472537439114527, -1.472658704414329, -1.47277989243983, -1.472885736044652, -1.472971982748238, -1.473057001985914, -1.473111001509433, -1.473183080752629, -1.473241129976461, -1.473241766991798, -1.473277552320077, -1.473298425126781, -1.473286006844108, -1.473290934817293, -1.473318161777614, -1.473337101295535, -1.473398271959067, -1.473479418711781, -1.47356580847938, -1.473798123441489, -1.473943559330443, -1.474114859182727, -1.474296222617526, -1.474441914037075, -1.47456580590815, -1.474747126870469, -1.474875773475041, -1.475040161356389, -1.475187799242784, -1.475330945481803, -1.475444365622718, -1.475666140000211, -1.475810155510329, -1.475876619415141, -1.475941950250361, -1.476027874360672, -1.476096249988447, -1.476176585124252, -1.476237521656968, -1.476339666840722, -1.476470490897499, -1.476588149573653, -1.476643581029263, -1.476711128159579, -1.476777291217415, -1.47688461732589, -1.477008586167409, -1.477106357485791, -1.477235909469932, -1.477346301642242, -1.477491796795489, -1.47760460913878, -1.477693129200662, -1.477784118881864, -1.477839538753542, -1.477871236612317, -1.477901340121204, -1.477901386308062, -1.477868296260728, -1.477811979915548, -1.477722369149709, -1.477536663712736, -1.477343733836881, -1.477234147157211, -1.477079417047327, -1.476910358163645, -1.476781368065636, -1.476659252227264, -1.476524583250806, -1.476434562222894, -1.476248158755969, -1.476121915175501, -1.475958978252887, -1.475812901928989, -1.475669826308685, -1.475547552448289, -1.475340439131063, -1.475127945673369, -1.474898446294284, -1.474702983479134, -1.474506656172359, -1.474499362661599, -1.474323591114902, -1.474141157922188, -1.473954835494809, -1.473793363937381, -1.473599158642731, -1.473397766343604, -1.473242138705611, -1.473065858964625, -1.472868532103631, -1.472641343145586, -1.47249624765426, -1.47235747397613, -1.472273723050596, -1.472209762956716, -1.472188012707729, -1.472238835872575, -1.472288082352148, -1.472322791105026, -1.472390187597973};


    public static final double[] stopsX = {-1.469727,-1.472745,-1.472965,-1.476600,-1.464255 };
    public static final double[] stopsY = {-48.446557,-48.451378,-48.451668,-48.454826,-48.444712 };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //barra superior
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        status = (TextView) findViewById(R.id.status);
        viewMessage = (TextView) findViewById(R.id.message);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        busOn = true;
        animacao a = new animacao();
        a.start();


        //mqttconnect - objeto para conexão com o MQTT em um thread separado
        mqttconnect = new MqttConnect(this.getApplicationContext(),handler);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mqttconnect.doConnect();
            }
        }).start();


    }

    @Override
    protected void onStop() {
        super.onStop();
        pauseMapService();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ReloadMapService();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //marcar a rota do Circular
        traceRoute();

        //definir os pontos de parada
        setStops();


        LatLng place = new LatLng(rotaY[0], rotaX[0]);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, null);

    }


    /**
     * Traça a rota do circular no mapa
     */
    private void traceRoute () {

        ArrayList<LatLng> pontos= new ArrayList<>();

        for (int i=0; i<NCircularPoints; i++) {
            pontos.add(new LatLng(rotaY[i], rotaX[i]));
        }
        pontos.add(new LatLng(rotaY[0], rotaX[0]));

            Polyline line = mMap.addPolyline(new PolylineOptions().addAll(pontos).width(5).color(Color.rgb(255,153,153)));

        }

    //posiciona os pontos de parada
    public void setStops() {

        for (int i = 0; i<NStopPoints; i++) {
            Marker stop = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(stopsX[i],stopsY[i]))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("pinstop",50,50))).flat(true));
        }


    }

    private void pauseMapService() {

        //pausar o serviço de atualização
        mMap.clear();                                    //limpa marcadores do mapa
        repositorioCirculares.removeAllCircularMarks();  //remove todos os marcadores da lista de circulares
        busOn = false;                                   //para o serviço de atualização


    }

    private void ReloadMapService() {

        //reiniciar o serviço de atualização

        setStops();                                      //insere os pontos de parada
        traceRoute();                                    //traça a rota do circular
        busOn = true;                                    //flg de inicio da animação ativava
        animacao a = new animacao();                     //instancia o thread da animação
        a.start();                                       //incia a animação


    }


    private void checkConectivity () {

        if (mqttconnect.isconnected()) {
            status.setText("Conectado");
            status.setTextColor(Color.GREEN);
        } else {
            status.setText("Desconectado");
            status.setTextColor(Color.RED);
        }

    }


    /**
     * updateCircularPosition()
     * Atualiza os pontos do circular no mapa
     *
     */
    private void updateCircularPosition(){

        //atualiza a lista de circulares
        repositorioCirculares.UpdateCircularList();

        for (Circular C : repositorioCirculares.getCircularList()) {
            if (C.getMarcador()==null) {
                //se o circular não tem marcador - cria um marcador
                Marker circular = mMap.addMarker(new MarkerOptions()
                        .position(C.getPosition())
                        .title(C.getNome())
                        .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("circ",100,100)))
                );
                C.setMarcador(circular);
            } else {
                //se o circular já tem marcador atualizar posição
                 C.getMarcador().setPosition(C.getPosition());
            }

            //verifica se a informação é antiga para apagar
            if (C.isErase())
            {
                //remove marcador do mapa
                C.getMarcador().remove();
                //remove da lista
                repositorioCirculares.getCircularList()
                        .remove(repositorioCirculares.getCircularList().indexOf(C));
            }

            //verifica se a informação é antiga para marcar cinza
            if (C.isObsolet()) {
                C.getMarcador().setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("circg",100,100)));

            }
            //verifica se a informação foi renovada
            if (!C.isObsolet()) {
                C.getMarcador().setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("circ",100,100)));

            }


        }

    }


    //redimenciona os icones do mapa para tamanhos personalizados
    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


    private void PublishMessage(String message) {
        Snackbar.make(findViewById(R.id.cood), message, Snackbar.LENGTH_LONG).show();

    }


    //classe privada para a atualização deste fragment
    private class animacao extends Thread {
    @Override
    public void run() {
        while (busOn) {
            try {
                sleep(timeWait);

            } catch (Exception e) {

            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    updateCircularPosition(); //atualiza a posição do circular
                    checkConectivity();//checa a conectividade

                }
            });


        }

    }
};







}
