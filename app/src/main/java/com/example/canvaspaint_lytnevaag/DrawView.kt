package com.example.canvaspaint_lytnevaag

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs

// Рисовалка
// Аннотация нужна для того чтобы нам не создавать кучу конструкторов как в java, потому что у вьюхи
// в Java есть 4 конструктора, каждый из них содержит различный набор параметров контекста, атрибутов,
// дефолтных стилей. И чтобы это не писать за нас это делает  @JvmOverloads
// это перегрузка конструктора, которая позволяет уже пользоваться определенным конструктором
// на этапе вызова каких то параметров он будет понимать  и сам автоматически сгенерит то, что нам надо
class DrawView @JvmOverloads constructor(
    context: Context,
    // указаны команды о том, как будут выглядеть наши вьюхи, чтобы дефолтные аттрибуты не отражались
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
)
    //наследуются от View, в изненном цикле есть функция Measure, где есть константные параметры,
    // которы мы можем задать самостоятельно
    : View(context, attrs, defStyleAttr) {
    companion object {
        private const val STROKE_WIDTH = 12f
    }

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    //У нас есть выбранный цвет, который парсится из входящего значения  сначала черным,
    // Тему мы применяем никакую. чт он никаких дефолтных значений не подтягивал
    private var drawColor = ResourcesCompat.getColor(resources, COLOR.BLACK.value, null)

    //Есть путь, это набор векторов, который определяется движение
    private var path = Path()
    //xy - event а от пользователя
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    // ныненшний xy
    private var currentX = 0f
    private var currentY = 0f
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    // Path representing
    private val drawing = Path() // the drawing
    private val curPath = Path() // what's currently being drawn

    //Онклики, чтобы скрывалась нижняя плашка, когда регистрируется клик
    private var onClick: () -> Unit = {}

    // Painting Settings
    // конфигурируем как выглядит линия и как мы будем рисовать

    private val paint = Paint().apply {
        color = drawColor // цвет линии
        isAntiAlias = true // сглаживание
        // Smooths out edges of what is drawn without affecting shape.
        isDither =
            true //связано чото с оптимизацией, можно жить и без нее
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        style = Paint.Style.STROKE //стиль,то, как выглядит линия
        // default: FILL
        strokeJoin = Paint.Join.ROUND // то, как соединяется
        // default: MITER
        strokeCap = Paint.Cap.ROUND // то, чем пишем (круг как у шариковой ручки в этом случае)
        // default: BUTT
        strokeWidth = STROKE_WIDTH // и ширина default: Hairline-width (really thin)
    }
        // Рендер, рассматриваем MVI архитектуру
    fun render(state: CanvasViewState) {
        drawColor = ResourcesCompat.getColor(resources, state.color.value, null)
        paint.color = drawColor
        paint.strokeWidth = state.size.value.toFloat()
        if (state.tools == TOOLS.DASH) {
            paint.pathEffect = DashPathEffect(
                floatArrayOf(
                    state.size.value.toFloat() * 2,
                    state.size.value.toFloat() * 2,
                    state.size.value.toFloat() * 2,
                    state.size.value.toFloat() * 2
                ), 0f
            )
        } else {
            paint.pathEffect = null
        }
    }

    //Удаление и инвалидация, удаляет все что было,покрываетColor.TRANSPARENT(невидимым)
    // PorterDuff.Mode.CLEAR - очищена память
    fun clear() {
        extraCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        invalidate()
    }

    fun setOnClickField(onClickField: () -> Unit) {
        onClick = onClickField
    }

    // регистрирует нажатие пользователя, ведет палец по экрану/снял палец и т.д.
    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    //Сбрасывает позицию
    private fun restartCurrentXY() {
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    //Реализация всех действий, которые нужно совершить
    private fun touchStart() {
        onClick()
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        restartCurrentXY()
    }

    private fun touchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            path.quadTo(
                currentX,
                currentY,
                (motionTouchEventX + currentX) / 2,
                (motionTouchEventY + currentY) / 2
            )
            restartCurrentXY()
            extraCanvas.drawPath(path, paint)
            extraCanvas.save()
        }
        invalidate()
    }

    private fun touchUp() {
        drawing.addPath(curPath) //добавляет путь, пока не отрисовалось, запоминает движения,
        // те которые были произведены пальцем и только потом добавляет их в путь
        curPath.reset()
    }

        //
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
    }

    //Самое главное: onDraw - он как раз занимается прорисовкой(последний метод, перед
    // тем как увидит пользователь
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //рисуем битмап
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
        //рисуем путь. который нарисовал пользователь
        canvas.drawPath(drawing, paint)
        //отражает, что в данный момент было нарисовано
        canvas.drawPath(curPath, paint)
    }
}