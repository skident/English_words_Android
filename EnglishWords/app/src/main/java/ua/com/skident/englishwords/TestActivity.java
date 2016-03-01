package ua.com.skident.englishwords;

import android.app.Activity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class TestActivity extends AppCompatActivity
{
    private GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        gestureDetector = new GestureDetector(this, new SwipeGestureDetector());

        TextView data = (TextView) findViewById(R.id.data);
        String html =
                "<!DOCTYPE html>\n" +
                        "<html lang='ru'>\n" +
                        "<head>\n" +
                        "<meta http-equiv='content-type' content='text/html; charset=UTF-8' />\n" +
                        "</head>\n" +
                        "<body id='page'>\n" +
                        "    <div class='base'>\n" +
                        "        <div class='base__left'>\n" +
                        "    <h1 class='title'>Present Simple - простое настоящее время</h1>\n" +
                        "\n" +
                        "    <div class='article'>\n" +
                        "        <p>\n" +
                        "\tВремя <strong>Present Simple</strong> обозначает действие в настоящем в широком смысле слова. Оно употребляется для обозначения обычных, регулярно повторяющихся или постоянных действий, например, когда мы говорим о чьих-либо привычках, режиме дня, расписании и т. д., т. е. <strong>Present Simple</strong> обозначает действия, которые происходят в настоящее время, но не привязаны именно к моменту речи.</p>\n" +
                        "<h2 class='mtitle'>\n" +
                        "\tОбразование Present Simple</h2>\n" +
                        "<p>\n" +
                        "\tУтвердительные предложения:</p>\n" +
                        "<table class='table table_bordered table_centered'>\n" +
                        "\t<tbody>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tI play</td>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tWe play</td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tYou play</td>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tYou play</td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tHe / she / it plays</td>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tThey play</td>\n" +
                        "\t\t</tr>\n" +
                        "\t</tbody>\n" +
                        "</table>\n" +
                        "<p>\n" +
                        "\tВопросительные предложения:</p>\n" +
                        "<table class='table table_bordered table_centered'>\n" +
                        "\t<tbody>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tDo I play?</td>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tDo we play?</td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tDo you play?</td>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tDo you play?</td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tDoes he / she / it play?</td>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tDo they play?</td>\n" +
                        "\t\t</tr>\n" +
                        "\t</tbody>\n" +
                        "</table>\n" +
                        "<p>\n" +
                        "\tОтрицательные предложения:</p>\n" +
                        "<table class='table table_bordered table_centered'>\n" +
                        "\t<tbody>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tI do not play</td>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tWe do not play</td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tYou do not play</td>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tYou do not play</td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tHe / she / it does not play</td>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\tThey do not play</td>\n" +
                        "\t\t</tr>\n" +
                        "\t</tbody>\n" +
                        "</table>\n" +
                        "<p>\n" +
                        "\t<a href='/grammar/english-verbs'>Английский глагол</a> во временной форме <strong>Present Simple</strong> почти всегда совпадает со своей начальной, то есть указанной в словаре, формой без частицы <b>to</b>. Лишь в 3-ем лице единственного числа к ней нужно прибавить окончание <b>-s</b>:</p>\n" +
                        "<div class='example'>\n" +
                        "\t<span class='eng'>I work &ndash; he work<b>s</b></span></div>\n" +
                        "<p>\n" +
                        "\tЕсли глагол оканчивается на <b>-s, -ss, -sh, -ch, -x, -o</b>, то к нему прибавляется окончание <b>-es</b>:</p>\n" +
                        "<div class='example'>\n" +
                        "\t<span class='eng'>I wish &ndash; he wish<b>es</b></span></div>\n" +
                        "<p>\n" +
                        "\tК глаголам на <b>-y</b> тоже прибавляется окончание <b>-es</b>, а <b>-y</b> заменяется на <b>-i-</b>:</p>\n" +
                        "<div class='example'>\n" +
                        "\t<span class='eng'>I try &ndash; he tr<b>ies</b></span></div>\n" +
                        "<p>\n" +
                        "\tДля того, чтобы построить <b>вопросительное предложение</b>, перед подлежащим нужно поставить вспомогательный глагол. Время <strong>Present Simple</strong> используется без него, поэтому в этом случае добавляется вспомогательный глагол <b>do </b>(или <b>does </b>в 3 л. ед. ч.):</p>\n" +
                        "<div class='example'>\n" +
                        "\t<span class='eng'><b>Do </b>you <b>like </b>rock?</span><br />\n" +
                        "\t<span class='rus'>Тебе нравится рок?</span><br />\n" +
                        "\t<br />\n" +
                        "\t<span class='eng'><b>Does </b>he <b>speak </b>English?</span><br />\n" +
                        "\t<span class='rus'>Он говорит по-английски?</span></div>\n" +
                        "<p>\n" +
                        "\tВ <b>отрицательных предложениях</b> тоже используется вспомогательный глагол <b>do</b>/<b>does</b>, но не перед подлежащим, а перед глаголом. После него прибавляется отрицательная частица <b>not</b>. <b>Do</b>/<b>does </b>и <b>not </b>часто сокращаются до <b>don&rsquo;t</b> и <b>doesn&rsquo;t</b> соответственно:</p>\n" +
                        "<div class='example'>\n" +
                        "\t<span class='eng'>I <b>do not like</b> black coffee.</span><br />\n" +
                        "\t<span class='rus'>Я не люблю черный кофе.</span><br />\n" +
                        "\t<br />\n" +
                        "\t<span class='eng'>She <b>doesn&#39;t</b> smoke.</span><br />\n" +
                        "\t<span class='rus'>Она не курит.</span></div>\n" +
                        "<div class='remark'>\n" +
                        "\tПримечание:</div>\n" +
                        "<p>\n" +
                        "\tВспомогательный глагол <b>do</b>/<b>does </b>может стоять и в утвердительных предложениях. Тогда предложение приобретает бoльшую экспрессивность, глагол оказывается эмоционально выделен:</p>\n" +
                        "<div class='example'>\n" +
                        "\t<span class='eng'>I <b>do want</b> to help you.</span><br />\n" +
                        "\t<span class='rus'>Я на самом деле хочу тебе помочь.</span><br />\n" +
                        "\t<br />\n" +
                        "\t<span class='eng'>Jane <b>does know</b> how to cook.</span><br />\n" +
                        "\t<span class='rus'>Джейн действительно умеет готовить.</span></div>\n" +
                        "<p>\n" +
                        "\tВ таких предложениях вспомогательный глагол никогда не сокращается.</p>\n" +
                        "<h2 class='mtitle'>\n" +
                        "\tСлучаи употребления Present Simple</h2>\n" +
                        "<ul class='list'>\n" +
                        "\t<li class='list__item'>\n" +
                        "\t\tРегулярные, повторяющиеся действия:</li>\n" +
                        "</ul>\n" +
                        "<div class='example'>\n" +
                        "\t<span class='eng'>I often <b>go </b>to the park.</span><br />\n" +
                        "\t<span class='rus'>Я часто хожу в парк.</span><br />\n" +
                        "\t<br />\n" +
                        "\t<span class='eng'>They <b>play </b>tennis every weekend.</span><br />\n" +
                        "\tКаждые выходные они играют в теннис.</div>\n" +
                        "<ul class='list'>\n" +
                        "\t<li class='list__item'>\n" +
                        "\t\tДействие в настоящем в широком смысле слова (не обязательно в момент речи):</li>\n" +
                        "</ul>\n" +
                        "<div class='example'>\n" +
                        "\t<span class='eng'>Jim <b>studies </b>French.</span><br />\n" +
                        "\t<span class='rus'>Джим изучает французский.</span><br />\n" +
                        "\t<br />\n" +
                        "\t<span class='eng'>We <b>live </b>in Boston.</span><br />\n" +
                        "\t<span class='rus'>Мы живем в Бостоне.</span></div>\n" +
                        "<ul class='list'>\n" +
                        "\t<li class='list__item'>\n" +
                        "\t\tОбщеизвестные факты:</li>\n" +
                        "</ul>\n" +
                        "<div class='example'>\n" +
                        "\t<span class='eng'>The Earth <b>is </b>round.</span><br />\n" +
                        "\t<span class='rus'>Земля &ndash; круглая.</span><br />\n" +
                        "\t<br />\n" +
                        "\t<span class='eng'>The Volga <b>is </b>the longest river in Europe.</span><br />\n" +
                        "\t<span class='rus'>Волга &ndash; самая длинная река в Европе.</span></div>\n" +
                        "<ul class='list'>\n" +
                        "\t<li class='list__item'>\n" +
                        "\t\tПеречисление последовательности действий:</li>\n" +
                        "</ul>\n" +
                        "<div class='example'>\n" +
                        "\t<span class='eng'>We <b>analyse </b>what our clients may need, <b>develop </b>a new product, <b>produce </b>a sample, <b>improve </b>it and <b>sell </b>it.</span><br />\n" +
                        "\t<span class='rus'>Мы анализируем, что может понадобиться нашим клиентам, разрабатываем новый продукт, изготавливаем образец, дорабатываем его и продаем.</span></div>\n" +
                        "<ul class='list'>\n" +
                        "\t<li class='list__item'>\n" +
                        "\t\tНекоторые случаи указания на будущее время (если имеется в виду некое расписание или план действий, а также в придаточных предложениях времени и условия):</li>\n" +
                        "</ul>\n" +
                        "<div class='example'>\n" +
                        "\t<span class='eng'>The airplane <b>takes off</b> at 2.30 p.m.</span><br />\n" +
                        "\t<span class='rus'>Самолет взлетает в 14:30.</span><br />\n" +
                        "\t<br />\n" +
                        "\t<span class='eng'>When you <b>see </b>a big green house, turn left.</span><br />\n" +
                        "\t<span class='rus'>Когда вы увидите большой зеленый дом, поверните налево.</span></div>\n" +
                        "<ul class='list'>\n" +
                        "\t<li class='list__item'>\n" +
                        "\t\tНекоторые случаи указания на прошедшее время (в заголовках газет, при пересказе историй):</li>\n" +
                        "</ul>\n" +
                        "<div class='example'>\n" +
                        "\t<span class='eng'>Airplane <b>crashes </b>in Pakistan.</span><br />\n" +
                        "\t<span class='rus'>В Пакистане разбился самолет.</span><br />\n" +
                        "\t<br />\n" +
                        "\t<span class='eng'>I met Lenny last week. He <b>comes </b>to me and says, &ldquo;Hello, mister!&rdquo;</span><br />\n" +
                        "\t<span class='rus'>На прошлой неделе я встретил Ленни. Подходит ко мне и говорит: &laquo;Здорово, мистер!&raquo;</span></div>\n" +
                        "    </div>\n" +
                        "            \n" +
                        "                    <div id='rontar_adplace_10330'></div>\n" +
                        "                    <script type='text/javascript'><!--\n" +
                        "                        (function (w, d, n) {\n" +
                        "                            var ri = { rontar_site_id: 1929, rontar_adplace_id: 10330, rontar_place_id: 'rontar_adplace_10330', adCode_rootUrl: 'http://adcode.rontar.com/' };\n" +
                        "                            w[n] = w[n] || [];\n" +
                        "                            w[n].push(\n" +
                        "                                ri\n" +
                        "                            );\n" +
                        "                            var a = document.createElement('script');\n" +
                        "                            a.type = 'text/javascript';\n" +
                        "                            a.async = true;\n" +
                        "                            a.src = 'http://adcode.rontar.com/rontar2_async.js?rnd=' + Math.round(Math.random() * 100000);\n" +
                        "                            var b = document.getElementById('rontar_adplace_' + ri.rontar_adplace_id);\n" +
                        "                            b.parentNode.insertBefore(a, b);\n" +
                        "                        })(window, document, 'rontar_ads');\n" +
                        "                    //--></script>\n" +
                        "                    <script type='text/javascript'><!--\n" +
                        "                        window['RontarParams'] = window['RontarParams'] || [];\n" +
                        "                        window['RontarParams'].push({ key: 'page', val: 'present-simple'});\n" +
                        "                    //--></script>\n" +
                        "                \n" +
                        "        <div id='yandex_ad_568e56719e676' style='margin:30px 0;'></div>\n" +
                        "</body>\n" +
                        "</html>";

        data.setText(Html.fromHtml(html));
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onLeftSwipe() {
        // Do something
        Log.d("a", "swipte to left");

    }

    private void onRightSwipe() {
        // Do something
        Log.d("a", "swipte to right");
    }

    // Private class for gestures
    private class SwipeGestureDetector
            extends GestureDetector.SimpleOnGestureListener {
        // Swipe properties, you can change it to make the swipe
        // longer or shorter and speed
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    TestActivity.this.onLeftSwipe();

                    // Right swipe
                } else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    TestActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }
}