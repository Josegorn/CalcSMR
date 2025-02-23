package smr.mme.calculadora

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlin.math.absoluteValue

const val DIV0: String = "ERROR# División por cero"
const val OVERFLOW = "ERROR# Overflow"
const val NMAX = 999999999999999999L

class MainActivity : AppCompatActivity() {

    private var but0: Button? = null
    private var but1: Button? = null
    private var but2: Button? = null
    private var but3: Button? = null
    private var but4: Button? = null
    private var but5: Button? = null
    private var but6: Button? = null
    private var but7: Button? = null
    private var but8: Button? = null
    private var but9: Button? = null

    private var butSuma: Button? = null
    private var butRest: Button? = null
    private var butProd: Button? = null
    private var butDiv: Button? = null
    private var butMod: Button? = null
    private var butX2: Button? = null
    private var butX1024: Button? = null
    private var butD2: Button? = null
    private var butD1024: Button? = null
    private var butSigno: Button? = null

    private var butDel: Button? = null
    private var butReset: Button? = null

    private var butAns: Button? = null
    private var butIgual: Button? = null

    private var lcd: TextView? = null

    private var num: Long = 0L
    private var ans: Long = 0L
    private var operador: OP = OP.NOP
    private var error: Boolean = false

    private enum class OP {
        NOP, SUMA, REST, PROD, DIV, MOD, X2, X1024, D2, D1024
    }

    private enum class T {
        N0, N1, N2, N3, N4, N5, N6, N7, N8, N9
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, View(this)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        but0 = findViewById(R.id.b0)
        but1 = findViewById(R.id.b1)
        but2 = findViewById(R.id.b2)
        but3 = findViewById(R.id.b3)
        but4 = findViewById(R.id.b4)
        but5 = findViewById(R.id.b5)
        but6 = findViewById(R.id.b6)
        but7 = findViewById(R.id.b7)
        but8 = findViewById(R.id.b8)
        but9 = findViewById(R.id.b9)

        butSuma = findViewById(R.id.bSum)
        butRest = findViewById(R.id.bRes)
        butProd = findViewById(R.id.bPro)
        butDiv = findViewById(R.id.bDiv)
        butMod = findViewById(R.id.bMod)
        butX2 = findViewById(R.id.bx2)
        butX1024 = findViewById(R.id.bx1024)
        butD2 = findViewById(R.id.bmit)
        butD1024 = findViewById(R.id.bDiv1024)
        butSigno = findViewById(R.id.bneg)

        butDel = findViewById(R.id.bDEL)
        butReset = findViewById(R.id.bReset)

        butAns = findViewById(R.id.bAns)

        butIgual = findViewById(R.id.beq)

        lcd = findViewById(R.id.LCD)
        wLCD(0L)

        but0?.setOnClickListener { botonNum(T.N0.ordinal) }
        but1?.setOnClickListener { botonNum(T.N1.ordinal) }
        but2?.setOnClickListener { botonNum(T.N2.ordinal) }
        but3?.setOnClickListener { botonNum(T.N3.ordinal) }
        but4?.setOnClickListener { botonNum(T.N4.ordinal) }
        but5?.setOnClickListener { botonNum(T.N5.ordinal) }
        but6?.setOnClickListener { botonNum(T.N6.ordinal) }
        but7?.setOnClickListener { botonNum(T.N7.ordinal) }
        but8?.setOnClickListener { botonNum(T.N8.ordinal) }
        but9?.setOnClickListener { botonNum(T.N9.ordinal) }

        butSuma?.setOnClickListener { botonOpB(OP.SUMA) }
        butRest?.setOnClickListener { botonOpB(OP.REST) }
        butProd?.setOnClickListener { botonOpB(OP.PROD) }
        butDiv?.setOnClickListener { botonOpB(OP.DIV) }
        butMod?.setOnClickListener { botonOpB(OP.MOD) }
        butX2?.setOnClickListener { botonOpM(OP.X2) }
        butX1024?.setOnClickListener { botonOpM(OP.X1024) }
        butD2?.setOnClickListener { botonOpM(OP.D2) }
        butD1024?.setOnClickListener { botonOpM(OP.D1024) }
        butSigno?.setOnClickListener { botonSigno() }
        butDel?.setOnClickListener { botonDel() }
        butReset?.setOnClickListener { botonReset() }
        butAns?.setOnClickListener { botonAns() }
        butIgual?.setOnClickListener { botonIgual() }
    }

    private fun leerLCDn(): Long {
        return lcd?.text.toString().toLong()
    }

    private fun leerLCDs(): String {
        return lcd?.text.toString()
    }

    private fun wLCD(n: Long) {
        n.toString().also { lcd?.text = it }
    }

    private fun botonNum(digit: Int) {
        if (error) return
        if (num == 0L) {
            num = digit.toLong()
        } else {
            if (isOverflow(leerLCDn() * 10)) return
            num = leerLCDn() * 10 + digit
        }
        wLCD(num)
    }

    private fun botonOpB(op: OP) {
        if (error) return
        if (ans != 0L) {
            cBinario()
        }
        ans = leerLCDn()
        num = 0L
        operador = op
    }

    private fun botonDel() {
        if (error) {
            botonReset()
            return
        }
        if (num.absoluteValue >= 10) {
            lcd?.text = leerLCDs().dropLast(1)
            num = leerLCDn()
        } else {
            wLCD(0L)
            num = 0L
        }
    }

    private fun botonReset() {
        with(this) {
            error = false
            lcd?.gravity = Gravity.END
            wLCD(0L)
            operador = OP.NOP
            num = 0L
            ans = 0L
        }
    }

    private fun botonAns() {
        if (error) return
        wLCD(ans)
        ans = num
        num = leerLCDn()
    }

    private fun cBinario() {
        if (error) return
        num = leerLCDn()
        if (isDiv0()) {
            lcd?.text = DIV0
            lcd?.gravity = Gravity.CENTER_HORIZONTAL
            error = true
            return
        }
        val resultado = when (operador) {
            OP.SUMA -> ans + num
            OP.REST -> ans - num
            OP.PROD -> ans * num
            OP.DIV -> ans / num
            OP.MOD -> ans % num
            else -> num
        }
        if (isOverflow(resultado)) {
            lcd?.text = OVERFLOW
            lcd?.gravity = Gravity.CENTER_HORIZONTAL
            error = true
        } else {
            wLCD(resultado)
        }
    }

    private fun botonIgual() {
        cBinario()
        operador = OP.NOP
        num = 0L
    }

    private fun botonOpM(op: OP) {
        if (error) return
        num = leerLCDn()
        val resultado: Long = when (op) {
            OP.X2 -> num * 2
            OP.X1024 -> num * 1024
            OP.D2 -> num / 2
            OP.D1024 -> num / 1024
            else -> num
        }
        if (isOverflow(resultado)) {
            lcd?.text = OVERFLOW
            lcd?.gravity = Gravity.CENTER_HORIZONTAL
            error = true
        } else {
            wLCD(resultado)
            ans = num
        }
    }

    private fun botonSigno() {
        if (error) return
        num = leerLCDn()
        num = -num
        wLCD(num)

    }

    private fun isDiv0(): Boolean {
        return ((operador == OP.DIV) && (num == 0L))
    }

    private fun isOverflow(num: Long): Boolean {
        return (num.absoluteValue > NMAX)
    }
}
