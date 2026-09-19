package com.atlas.app

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.abs

class AtlasView(context: Context, private val actionCallback: (String) -> Unit) : View(context) {
    private val gold = Color.rgb(255, 181, 46)
    private val pale = Color.rgb(198, 220, 247)
    private val dim = Color.rgb(133, 148, 171)
    private val cyan = Color.rgb(42, 214, 255)
    private val green = Color.rgb(83, 244, 177)
    private val panel = Color.rgb(5, 9, 16)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mono = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
    private val monoBold = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
    private val sans = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
    private val sansBold = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
    private var orb: Bitmap? = null
    private var activityTitle = "Thinking"
    private var activityText = "Planning next steps…"
    private var pulse = 0f
    var onLaidOut: (() -> Unit)? = null

    init {
        setBackgroundColor(Color.BLACK)
        orb = BitmapFactory.decodeResource(resources, resources.getIdentifier("quantum_core", "drawable", context.packageName))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) { onLaidOut?.invoke() }

    fun activity(title: String, text: String) {
        activityTitle = title
        activityText = text
        invalidate()
    }

    override fun onDraw(c: Canvas) {
        super.onDraw(c)
        val s = width / 1024f
        val sy = s
        c.save(); c.scale(s, sy)
        drawBackground(c)
        drawHeader(c)
        drawSideNav(c)
        drawStatusLegend(c)
        drawOrb(c)
        drawPromptPanel(c)
        drawRecent(c)
        drawBottomNav(c)
        c.restore()
        pulse += 0.035f
        postInvalidateDelayed(40)
    }

    private fun drawBackground(c: Canvas) {
        val g = RadialGradient(512f, 670f, 600f, intArrayOf(Color.rgb(18,12,3), Color.BLACK), floatArrayOf(0f, .65f), Shader.TileMode.CLAMP)
        paint.shader = g; c.drawRect(0f,0f,1024f,1536f,paint); paint.shader=null
        paint.style=Paint.Style.STROKE; paint.strokeWidth=1f; paint.color=Color.argb(80,255,181,46)
        c.drawLine(0f,163f,1024f,163f,paint); c.drawLine(0f,1416f,1024f,1416f,paint); paint.style=Paint.Style.FILL
    }

    private fun drawHeader(c: Canvas) {
        drawAtlasMark(c, 82f, 104f, 28f)
        text(c,"A . T . L . A . S .",162f,101f,35f,gold,sansBold)
        text(c,"ADVANCED TACTICAL LOGIC & ASSISTANCE SYSTEM",162f,137f,12f,dim,mono)
        roundRect(c,704f,76f,978f,141f,34f,Color.TRANSPARENT,Color.rgb(31,111,103),1.2f)
        dot(c,738f,108f,10f,green)
        text(c,"CONNECTED",760f,104f,14f,green,monoBold)
        text(c,"Hermes Online",760f,125f,11f,dim,mono)
        circle(c,956f,107f,27f,Color.TRANSPARENT,Color.rgb(255,181,46),1f); gear(c,956f,107f,13f,gold)

        roundRect(c,46f,165f,978f,284f,28f,Color.argb(45,0,0,0),Color.rgb(112,72,23),1f)
        infoBlock(c,78f,"MODEL","Claude 3.5 Sonnet","(OpenRouter)","brain")
        infoBlock(c,430f,"PROVIDER","OpenRouter","Online","globe")
        infoBlock(c,776f,"SESSION","Active","2h 14m","database")
    }

    private fun infoBlock(c: Canvas, x:Float, label:String, value:String, sub:String, icon:String) {
        circle(c,x+35f,224f,25f,Color.TRANSPARENT,pale,1f)
        when(icon){"brain"->brain(c,x+35f,224f,13f,pale);"globe"->globe(c,x+35f,224f,13f,pale);else->database(c,x+35f,224f,13f,pale)}
        text(c,label,x+72f,199f,12f,dim,mono)
        text(c,value,x+72f,224f,16f,pale,monoBold)
        text(c,sub,x+72f,246f,11f,dim,mono)
    }

    private fun drawSideNav(c: Canvas) {
        val items = listOf("CHAT","TERMINAL","FILES","TOOLS","SESSIONS","SETTINGS")
        val ys = floatArrayOf(350f,482f,594f,706f,818f,930f)
        for(i in items.indices){
            val active=i==0
            circle(c,90f,ys[i],44f,Color.TRANSPARENT,if(active) gold else pale,if(active) 2.4f else 1f)
            when(i){0->chat(c,90f,ys[i],18f,if(active) gold else pale);1->terminal(c,90f,ys[i],18f,pale);2->folder(c,90f,ys[i],19f,pale);3->tools(c,90f,ys[i],18f,pale);4->database(c,90f,ys[i],18f,pale);5->gear(c,90f,ys[i],18f,pale)}
            textCenter(c,items[i],90f,ys[i]+58f,13f,if(active) gold else pale,mono)
        }
    }

    private fun drawStatusLegend(c: Canvas) {
        val labels=listOf("IDLE","LISTENING","THINKING","EXECUTING","SPEAKING","APPROVAL","ERROR","OFFLINE")
        val ys=floatArrayOf(357f,419f,481f,543f,605f,667f,729f,791f)
        for(i in labels.indices){
            val col=when(i){0->gold;1->pale;2->pale;3->pale;4->pale;5->pale;6->pale;else->pale}
            circle(c,841f,ys[i],21f,Color.TRANSPARENT,col,1.2f)
            when(i){0->dot(c,841f,ys[i],11f,gold);1->mic(c,841f,ys[i],10f,pale);2->brain(c,841f,ys[i],11f,pale);3->gear(c,841f,ys[i],11f,pale);4->wave(c,841f,ys[i],12f,pale);5->shield(c,841f,ys[i],11f,pale);6->triangle(c,841f,ys[i],11f,pale);else->offline(c,841f,ys[i],11f,pale)}
            text(c,labels[i],881f,ys[i]+5f,14f,pale,mono)
        }
    }

    private fun drawOrb(c: Canvas) {
        val b=orb ?: return
        val src=Rect(0,0,b.width,b.height); val dst=RectF(180f,285f,845f,915f)
        paint.alpha=255; c.drawBitmap(b,src,dst,paint)
        textCenter(c,"READY WHEN YOU ARE",512f,926f,20f,gold,monoBold)
        textCenter(c,"Speak  ·  Type  ·  Command  ·  Create",512f,961f,13f,dim,mono)
    }

    private fun drawPromptPanel(c: Canvas) {
        roundRect(c,46f,1001f,978f,1168f,30f,Color.argb(130,2,6,12),Color.rgb(75,101,124),1f)
        circle(c,108f,1053f,32f,Color.TRANSPARENT,gold,1.7f); mic(c,108f,1053f,14f,gold)
        circle(c,920f,1053f,30f,Color.TRANSPARENT,gold,1.4f); send(c,920f,1053f,14f,gold)
        val chips=listOf("Build something","Search the web","Run a command","More")
        val xs=floatArrayOf(72f,328f,585f,814f); val ws=floatArrayOf(235f,229f,218f,138f)
        for(i in chips.indices){ roundRect(c,xs[i],1108f,xs[i]+ws[i],1152f,22f,Color.TRANSPARENT,Color.rgb(63,84,107),1f); textCenter(c,chips[i],xs[i]+ws[i]/2f,1136f,12f,pale,mono) }
        text(c,"✦",92f,1136f,14f,gold,monoBold); search(c,373f,1130f,9f,pale); terminal(c,630f,1130f,10f,pale); text(c,"•••",843f,1135f,13f,pale,monoBold)
    }

    private fun drawRecent(c: Canvas) {
        roundRect(c,46f,1188f,978f,1414f,26f,Color.argb(110,2,6,12),Color.rgb(52,72,94),1f)
        text(c,"▥",76f,1220f,20f,gold,monoBold); text(c,"Recent Activity",123f,1222f,16f,pale,monoBold); text(c,"View All",896f,1222f,12f,gold,mono)
        activityRow(c,1264f,"Terminal","npm run dev", "2m ago",gold,true)
        activityRow(c,1324f,"File Operation","Created: src/components/QuantumCore.tsx", "5m ago",gold,true)
        activityRow(c,1384f,"Thinking",activityText,"7m ago",cyan,false)
    }

    private fun activityRow(c:Canvas,y:Float,title:String,sub:String,time:String,col:Int,ok:Boolean){
        roundRect(c,77f,y-22f,112f,y+22f,12f,Color.TRANSPARENT,col,1f)
        if(title=="Terminal") terminal(c,94f,y,11f,col) else if(title=="Thinking") brain(c,94f,y,11f,col) else file(c,94f,y,11f,col)
        text(c,title,140f,y-2f,13f,pale,monoBold); text(c,sub,140f,y+17f,11f,dim,mono); text(c,time,925f,y-1f,10f,dim,mono)
        if(ok){ circle(c,971f,y,10f,Color.TRANSPARENT,green,1f); textCenter(c,"✓",971f,y+4f,10f,green,monoBold) } else { circle(c,971f,y,10f,Color.TRANSPARENT,cyan,1f) }
        paint.color=Color.rgb(30,40,52); paint.strokeWidth=1f; c.drawLine(78f,y+37f,970f,y+37f,paint)
    }

    private fun drawBottomNav(c: Canvas) {
        val items=listOf("Home","Chat","History","Profile"); val xs=floatArrayOf(128f,384f,640f,896f)
        for(i in items.indices){
            val active=i==0; val col=if(active) gold else pale
            when(i){0->home(c,xs[i],1452f,18f,col);1->chat(c,xs[i],1452f,18f,col);2->file(c,xs[i],1452f,18f,col);3->person(c,xs[i],1452f,18f,col)}
            textCenter(c,items[i],xs[i],1486f,12f,col,mono)
        }
        roundRect(c,374f,1518f,650f,1525f,4f,Color.rgb(80,84,92),Color.TRANSPARENT,0f)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if(e.action!=MotionEvent.ACTION_UP) return true
        val s=width/1024f; val x=e.x/s; val y=e.y/s
        when {
            x in 50f..140f && y in 315f..390f -> actionCallback("chat")
            x in 45f..140f && y in 430f..530f -> actionCallback("command")
            x in 45f..140f && y in 875f..980f -> actionCallback("settings")
            x in 65f..300f && y in 1100f..1165f -> actionCallback("build")
            x in 320f..570f && y in 1100f..1165f -> actionCallback("search")
            x in 575f..815f && y in 1100f..1165f -> actionCallback("command")
            x in 70f..145f && y in 1015f..1090f -> actionCallback("mic")
            x in 885f..955f && y in 1015f..1090f -> actionCallback("send")
        }
        return true
    }

    private fun text(c:Canvas,s:String,x:Float,y:Float,size:Float,color:Int,type:Typeface){paint.style=Paint.Style.FILL;paint.color=color;paint.textSize=size;paint.typeface=type;c.drawText(s,x,y,paint)}
    private fun textCenter(c:Canvas,s:String,x:Float,y:Float,size:Float,color:Int,type:Typeface){paint.textSize=size;paint.typeface=type;paint.color=color;paint.style=Paint.Style.FILL;c.drawText(s,x-paint.measureText(s)/2f,y,paint)}
    private fun roundRect(c:Canvas,l:Float,t:Float,r:Float,b:Float,rad:Float,fill:Int,stroke:Int,sw:Float){paint.style=Paint.Style.FILL;paint.color=fill;c.drawRoundRect(l,t,r,b,rad,rad,paint);if(sw>0){paint.style=Paint.Style.STROKE;paint.strokeWidth=sw;paint.color=stroke;c.drawRoundRect(l,t,r,b,rad,rad,paint);paint.style=Paint.Style.FILL}}
    private fun circle(c:Canvas,x:Float,y:Float,r:Float,fill:Int,stroke:Int,sw:Float){paint.style=Paint.Style.FILL;paint.color=fill;c.drawCircle(x,y,r,paint);if(sw>0){paint.style=Paint.Style.STROKE;paint.strokeWidth=sw;paint.color=stroke;c.drawCircle(x,y,r,paint);paint.style=Paint.Style.FILL}}
    private fun dot(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.FILL;paint.color=col;c.drawCircle(x,y,r,paint)}

    private fun drawAtlasMark(c:Canvas,x:Float,y:Float,r:Float){paint.style=Paint.Style.STROKE;paint.strokeWidth=4f;paint.color=gold;val p=Path();p.moveTo(x-r,y+r);p.lineTo(x,y-r);p.lineTo(x+r,y+r);p.lineTo(x,y+8);p.close();c.drawPath(p,paint);paint.style=Paint.Style.FILL}
    private fun chat(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=2f;paint.color=col;c.drawRoundRect(x-r,y-r*.75f,x+r,y+r*.65f,5f,5f,paint);val p=Path();p.moveTo(x-r*.2f,y+r*.65f);p.lineTo(x-r*.35f,y+r);p.lineTo(x+r*.05f,y+r*.65f);c.drawPath(p,paint)}
    private fun terminal(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=2f;paint.color=col;c.drawLine(x-r,y-r*.45f,x-r*.1f,y,paint);c.drawLine(x-r*.1f,y,x-r,y+r*.45f,paint);c.drawLine(x+r*.1f,y+r*.35f,x+r,y+r*.35f,paint)}
    private fun folder(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=2f;paint.color=col;c.drawRoundRect(x-r,y-r*.55f,x+r,y+r*.6f,3f,3f,paint);c.drawLine(x-r,y-r*.2f,x-r*.15f,y-r*.2f,paint)}
    private fun tools(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=2.2f;paint.color=col;c.drawLine(x-r,y+r,x+r,y-r,paint);c.drawCircle(x-r*.55f,y+r*.55f,r*.35f,paint);c.drawCircle(x+r*.55f,y-r*.55f,r*.35f,paint)}
    private fun database(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=1.7f;paint.color=col;c.drawOval(x-r,y-r*.65f,x+r,y-r*.1f,paint);c.drawArc(x-r,y-r*.4f,x+r,y+r*.7f,0f,180f,false,paint);c.drawArc(x-r,y-r*.05f,x+r,y+r*.95f,0f,180f,false,paint);c.drawLine(x-r,y-r*.4f,x-r,y+r*.5f,paint);c.drawLine(x+r,y-r*.4f,x+r,y+r*.5f,paint)}
    private fun brain(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=1.8f;paint.color=col;c.drawCircle(x-r*.45f,y,r*.65f,paint);c.drawCircle(x+r*.45f,y,r*.65f,paint);c.drawLine(x,y-r,x,y+r,paint)}
    private fun globe(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=1.5f;paint.color=col;c.drawCircle(x,y,r,paint);c.drawOval(x-r*.42f,y-r,x+r*.42f,y+r,paint);c.drawLine(x-r,y,x+r,y,paint)}
    private fun gear(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=2f;paint.color=col;c.drawCircle(x,y,r*.55f,paint);c.drawCircle(x,y,r*.18f,paint);for(i in 0..7){val a=i*Math.PI/4; c.drawLine(x+sin(a).toFloat()*r*.55f,y+(-Math.cos(a)).toFloat()*r*.55f,x+sin(a).toFloat()*r*.9f,y+(-Math.cos(a)).toFloat()*r*.9f,paint)}}
    private fun mic(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=2f;paint.color=col;c.drawRoundRect(x-r*.45f,y-r,x+r*.45f,y+r*.2f,5f,5f,paint);c.drawArc(x-r*.75f,y-r*.1f,x+r*.75f,y+r*.9f,0f,180f,false,paint);c.drawLine(x,y+r*.9f,x,y+r*1.35f,paint)}
    private fun wave(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=1.7f;paint.color=col;for(i in -2..2){c.drawLine(x+i*5f,y-r*(1f-abs(i)/3f),x+i*5f,y+r*(1f-abs(i)/3f),paint)}}
    private fun shield(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=1.8f;paint.color=col;val p=Path();p.moveTo(x,y-r);p.lineTo(x+r*.75f,y-r*.6f);p.lineTo(x+r*.6f,y+r*.55f);p.lineTo(x,y+r);p.lineTo(x-r*.6f,y+r*.55f);p.lineTo(x-r*.75f,y-r*.6f);p.close();c.drawPath(p,paint)}
    private fun triangle(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=1.8f;paint.color=col;val p=Path();p.moveTo(x,y-r);p.lineTo(x+r,y+r);p.lineTo(x-r,y+r);p.close();c.drawPath(p,paint)}
    private fun offline(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=1.7f;paint.color=col;c.drawLine(x-r,y-r,x+r,y+r,paint);c.drawLine(x+r,y-r,x-r,y+r,paint);c.drawCircle(x,y,r*.5f,paint)}
    private fun file(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=1.7f;paint.color=col;c.drawRect(x-r,y-r,x+r,y+r,paint);c.drawLine(x-r*.45f,y-r*.35f,x+r*.45f,y-r*.35f,paint);c.drawLine(x-r*.45f,y,x+r*.45f,y,paint)}
    private fun home(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.FILL;paint.color=col;val p=Path();p.moveTo(x-r*1.2f,y);p.lineTo(x,y-r);p.lineTo(x+r*1.2f,y);p.lineTo(x+r*.85f,y);p.lineTo(x+r*.85f,y+r);p.lineTo(x-r*.85f,y+r);p.lineTo(x-r*.85f,y);p.close();c.drawPath(p,paint)}
    private fun person(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=2f;paint.color=col;c.drawCircle(x,y-r*.55f,r*.35f,paint);c.drawArc(x-r,y-r*.05f,x+r,y+r*1.1f,200f,140f,false,paint)}
    private fun search(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=2f;paint.color=col;c.drawCircle(x,y,r,paint);c.drawLine(x+r*.7f,y+r*.7f,x+r*1.4f,y+r*1.4f,paint)}
    private fun send(c:Canvas,x:Float,y:Float,r:Float,col:Int){paint.style=Paint.Style.STROKE;paint.strokeWidth=2f;paint.color=col;val p=Path();p.moveTo(x-r,y+r*.1f);p.lineTo(x+r,y-r);p.lineTo(x+r*.2f,y+r);p.lineTo(x-r*.05f,y+r*.15f);p.close();c.drawPath(p,paint);c.drawLine(x-r*.05f,y+r*.15f,x+r,y-r,paint)}
}
