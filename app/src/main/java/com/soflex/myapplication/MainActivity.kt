package com.soflex.myapplication

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soflex.myapplication.ui.theme.MyApplicationTheme
import com.theeasiestway.opus.Constants.Channels.Companion.mono
import com.theeasiestway.opus.Constants.FrameSize.Companion._1920
import com.theeasiestway.opus.Constants.SampleRate.Companion._48000
import com.theeasiestway.opus.Opus


class MainActivity : ComponentActivity() {
    private lateinit var audioTrack: AudioTrack
    private var codec = Opus()

    private val listaDeCadenas = listOf(
        "80C186B597FE2813410F80EAAC7DBB6F28F171CAFFD8121FE4173AA75D258278000000000000000000000000000000",
        "80C186B598022813410041A7751941C4B6FA06849193A88333E4BD7DA860B45E0DB39C2FECC7EAC003550B84281738",
        "80C186B59806281301C63FC9F92F754337016B2550B9EDBB15EBCA4BD5064EA4D9A80CFD9AACD0784995D4D7AAB0E6",
        "80C186B5980A2810C64C31853CD23C90D9A12A1403F4245D98C26B3B5F4210A31D570ABEF0819ADC6034E1325D6C30",
        "80C186B5980E2810C846575AA354311B47482BA988AE37907CCFC6DCFCA1302482D357E93333F02E69FB1F84BAD5E8",
        "80C186B5981228134100CBEF153B9D20A1158DCD8A2178178DCE256C7295D778C48C82FDB8D1761AF225D7D7242F30",
        "80C186B5981628134100C2C86321AED8B05E3C3EDA3CD235DF2EA729102C6F910238001E88C3F685DB3AE66E6F8440",
        "80C186B5981A2810C26EA4A3DC22284C12801F56523D1BF5BA2BA1A85E14C9E94A32A97E5A82911F97696AA82B271C",
        "80C186B5981E28134100CB18D592466952FBEE7393743E77B2EF6FADA6C7BAD8D2907DB95B9FD0B4B975EFC1DE4BC2",
        "80C186B598262810C8B429ECC51F7F5BCA496EFC15C1DD83808E9D16FA5D2A0D1477F764CCADACAB8E0B9A259BB860",
        "80C186B59822281301C9CA9BAA8C28B6B66B4EACEC45D8D8C6713783CA963909159B8968136F16717AEF94F7A160A8",
        "80C186B5982A2810C7E4B1583FCAD20F313E3C5E36643B9F31A895E353410A8C7F89C930BB537B41EAF5E2A7E944A5",
        "80C186B5982E281301C78FB8E493E97BB1EB0B4067C459C12F0905DC5B590F73763B9A233A5C054D2839B7C4B73816",
        "80C186B598322810C141EE3E3A7947A7CB4DFC046CD8E14151FFD8199A9590017B6A7EF9FC6E70A60092DB901036DC",
        "80C186B5983628134100C63F7A17D67B1C9008619284281A70E7908A54F7DA27CE9600142A48B2BB6A9DB7A83BED6C",
        "80C186B5983A281301C557022FC08FE4839CE0A348D05D202F8E42748963BE47FB73539EBB81FCFFDFE2D763516823",
        "80C186B5983E2810C4F6C182AED8CE3F92429621390F2C6B02CCF60B755B0B3CF09A5FECF74C1CA74CC1868AF90AF2",
        "80C186B59842281301CA93C02E5172D2ABFFA140ED6A9E1A99B2613FFA082092658D3BFA4A4EF2E235F4B13C5C4D80",
        "80C186B5984628134100C20AE07CBECA4A6F6DEBB38D703486A5BEE284A2B5DCFE7C9C9A7D705821983BB87FE7B21D",
        "80C186B5984A281301C283B72D9CCE03CA69F6FFE6C6B3967C47C91BFE919C330DE6B3CA7D072C03BE5178514AF6C0",
        "80C186B5984E28134100CA011D7A9BD92A1A60564EFB95C785D27B2840C33689A6F163B36630EA4F8BAA561519E230",
        "80C186B59852281301C1FAC3893FAD80223E5988F769E0A7560128A83F59DEE49DCCC17B311CB185561AC1434FF79E",
        "80C186B5985628134101CA357166ED847605E273ED132B420BF5D29B9A2169556ACE33DCB8923BE2587F83C19E2E00",
        "80C186B5985A28134101C225B7A8543670BF0C71C89DE5C212391B62E878240500280796FBBA662CC4C3DEA079B000",
        "80C186B5985E2810C25AEAE062280595BEE03DA52048B642273D8858B464A1D4819D3CA8632915371EA4FBF8992780",
        "80C186B598622810CBC4A54D186E888B79373892317F769DFA62C9F4F44C3678B143AE84CF615F8DD4F1A07CDBEF90",
        "80C186B598662810CB9DDADB90A4D23834602A6EDB559BA660CDD1013F8D3820AD0303E763F9AEAD9313B9ADDDA186",
        "80C186B5986A2810CB98EA8A77E1E281BCA51548F2130969217277A58DE7158D8A3755AF82F1962352FA63390DD2C0",
        "80C186B5986E281301C2C9C0FE6083B78428C08BA59ED825AED2B09FA9E78AE4EF165360FF1FDA6050E85A89498558",
        "80C186B598722810DE8ABFD76CD3F19F7B9D472C3257AEF13D3C226C0B6920053C17439584E7AE1FC4E81897321840",
        "80C186B598762810DE38AF47FEBBA94E413F9D4470EB211E2855A922029923E96ABB2E9AA1E7A05574CE666B250C11",
        "80C186B5987A2810DE771C2B09FF2A719547240CAD130622C439C8773D27A7F9DA0E6805C875745687B52C6D7D9EB8",
        "80C186B5987E281301DE1660F3E70660E362CF609FE6DA9604F1FB8B4217E4F50DAE812E011196D52A9E77F96BA082",
        "80C186B598822810DE8246709332A5C60A03B369557D8A30FB285B5F5D5D54B86DFF7584B0A49ED8777FA72708B8C8",
        "80C186B59886281301DE730142B6FB1BA4D47058EE8B70447C471FBA3188B7B743C006ADC4CF9595489F1096730A63",
        "80C186B5988A28134101DDBA033A9718285F9F845146E8F1884DEEB827382838F907E3E409DDA221A688AA1AE6AA00",
        "80C186B5988E28134100DCDE4D5BB199FE771388ABF4FA9A7BE9CFCF4B7F714AAAC329F0C9A6E32868E0F0174EFAA0",
        "80C186B598922810DE3F57C6177A4DA808120111BD6AB83255FC59E3666C1EDD336E83A8D973FEB603C30BCFE921B0",
        "80C186B598962810DE0122DC26F63172F293F09C00C6085F5C1B0838103E6874E46A55EADAFE14E69D5754FD4D6328",
        "80C186B5989A2810DE427245F6CDF03A9ECBFA3B7B249296C6A405D9D4328D27B91B51E13D72B6A102C97FEB4160E0",
        "80C186B5989E28134100DE2554EB75EE147B31783D90869AE66917ABF2CF1EC6612BE0506FA6C1B52F290E9FAE3435",
        "80C186B598A2281301DE0CA2FDAE955707E5AB1A09BFD22B14BAA70D3CAF536EB4F475605B717C28A9C229DDA7DE1C",
        "80C186B598A628134102DD48A5DE40BC81B1F1E0D8347F66353172465C172B7E3D4C0E057E4113F1AD3612D3100000",
        "80C186B598AA2810DCAA3B26475BBA8CE9F3C98DA49D4ABD19500F4C2D489F2E048C2367A11BAE39C88B4A34E948C0",
        "80C186B598AE2810DE6057A9325055F9DDFC910CDA8E6B93A4E0CD9D8076C3B957BB7F6190F4C994D9ACA521F7C0EE",
        "80C186B598B22810DE9120D6E04991F5EA4E8583DAEC4E7A4697E5B09D1733C5953E56452BEFB1A7AFE30927589A7F",
        "80C186B598B628134100DE6386036C48DF2377DB9F211ABA509B3959B048016C84010676754BB9B9613C4FC4E034C0",
        "80C186B598BA2810DE82A1A3785F4BE52F088C40F2962C02FF06C42162120834264DBC5186076DE229C6E8E32A8694",
        "80C186B598BE2810CBD91828889B8BC22F36A1FAB12D9A4DBC962E82DF3D960A96464C73E3DEBD7CC34C38A1924070",
        "80C186B598C2281301CA63391DC9C7CFE77A5D35632F7AEF58AAF640E61B274944AC24EBAC7D15796AF2C850939048",
        "80C186B598C62810C2D16EF972E7FC6522EB1B98511A8CF9A9DDA38B4772CE49844C4BACA46DBF7F3F809D53DB1F4C",
        "80C186B598CA2810DE8BD868476F42F1E79D222745B860EDA5586F05835A62FC636E1A6993D874CDE2607B39417ACC",
        "80C186B598CE2810DE8B0488281E425F01185C70DADCD30579EEFF21FF1D6B3DEAAD300D9D0D0B6B3450138AC59D40",
        "80C186B598D22810DCEE5BEED36F8AAA3DD70A50ED96FEDD4A87066B1FF67482F510831FC998994544FB76F5DCBCDC",
        "80C186B598D62810C243CE61B292238ACEAC5AF85B3B08B0211D47189C24734E64E16811DA8B66DBC51D617C39DBD2",
        "80C186B598DA281301C905B28AAB28A389A82F456AF1CAAAA6ABE5A4F9895132949588C80661319369790DDC873238",
        "80C186B598DE281301C8A14BA4FB2A2D2E6D4785457084F2D259133F6C527BC3A77A600C11B18B5F475087CCD7E98E",
        "80C186B598E0A028134106C7A07ABA51E832D849C5D5D269755F48F0C0432E6E6001BFDF0678BE4909F7000000000000"
    )
    private val lista = mutableListOf<ByteArray>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("ok")
                }
            }
        }


    }


    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
        ) {
            Button(onClick = {
                        load()
                    })
            {
                Text(text = "LOAD")
            }
            Button(onClick = {
                    decodeOpus()
                    println("decodeOpus FIN")
            }) {
                Text(text = "PLAY")
            }
        }

    }


    private fun initAudio() {

        codec.decoderInit(
            _48000(),
            mono()
        )


        val sampleRate = 48000
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        val audioFormat = AudioFormat.Builder()
            .setSampleRate(sampleRate)
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()

        audioTrack = AudioTrack(
            audioAttributes,
            audioFormat,
            bufferSize,
            AudioTrack.MODE_STREAM,
            0
        )

        audioTrack.play()
        println("initAudio OK")
    }

    private fun load(): Boolean {
        println("LOAD...")

        fun stringToByteArray(string: String): ByteArray {
            val byteArray = ByteArray(string.length / 2)
            for (i in string.indices step 2) {
                byteArray[i / 2] = string.substring(i, i + 2).toInt(16).toByte()
            }

            return byteArray
        }

        for (hexString in listaDeCadenas) {
            lista.add(stringToByteArray(hexString.substring(14)))
        }

        println("LOAD OK")

        initAudio()

        return true
    }

    private fun decodeOpus() {
        var total = 0
        try {
        for ((fila, ba) in lista.withIndex()) {
            val decoded: ByteArray? = codec.decode(ba, _1920())
            if (decoded != null) {
                var aux = 0
                for (dato in decoded) {
                    aux += dato
                }
                total += aux
                println("fila $fila aux: $aux tot: $total")

                //audioTrack.write(decoded, 0, decoded.size)
            }
        }

        } catch (e: Exception) {
            println("DECODE ERR " + e.message)
            Thread.sleep(100)
        }
    }
}