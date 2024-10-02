package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding
    private var canaddoperation=false
    private var canadddecimal=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun numberAction(view: View){
        if(view is Button){
            binding.expression.append(view.text)
            canaddoperation=true
        }
    }

    fun operationAction(view: View){
        if(view is Button && canaddoperation){
            binding.expression.append(view.text)
            canaddoperation=false
            canadddecimal=true
        }
    }

    fun allClearAction(view: View){
        binding.expression.setText(getString(R.string.empty_text))
        binding.result.setText(getString(R.string.empty_text))
    }

    fun backspaceAction(view: View){
        val len=binding.expression.length()
        if(len>0){
            binding.expression.text=binding.expression.text.subSequence(0,len-1)
        }
    }

    fun equalsAction(view : View){
        binding.result.text=calculatetheexpression()
    }

    private fun calculatetheexpression():String{
        val list=digitoperator()
        if(list.isEmpty())return ""

        val multdiv=calculatemultdiv(list)
        if(multdiv.isEmpty())return ""

        val result=addsub(multdiv)
        return result.toString()
    }

    private fun addsub(list: MutableList<Any>): Float {
        var result=list[0] as Float
        for(i in list.indices){
            if(list[i] is Char && i!=list.lastIndex){
                val oper=list[i]
                val next=list[i+1] as Float
                if(oper=='+'){
                    result+=next
                }
                else if(oper=='-'){
                    result-=next
                }
            }
        }

        return result
    }

    private fun calculatemultdiv(list: MutableList<Any>): MutableList<Any> {
        var list1=list
        while(list1.contains('x')|| list1.contains('/')){
            list1=multdiv(list1)
        }

        return list1
    }

    private fun multdiv(list: MutableList<Any>): MutableList<Any> {
        val newlist= mutableListOf<Any>()
        var restartindex=list.size

        for(i in list.indices){
            if(list[i] is Char && i!=list.lastIndex && i<restartindex){
                val oper=list[i]
                val prev=list[i-1] as Float
                val next=list[i+1] as Float

                when (oper){
                    'x'->
                    {
                        newlist.add(prev*next)
                        restartindex=i+1
                    }
                    '/'->
                    {
                        newlist.add(prev/next)
                        restartindex=i+1
                    }
                    else->
                    {
                        newlist.add(prev)
                        newlist.add(oper)
                    }
                }
            }

            if(i>restartindex){
                newlist.add(list[i])
            }
        }

        return newlist
    }


    private fun digitoperator():MutableList<Any>{
        val li= mutableListOf<Any>()
        var current=""

        for(i in binding.expression.text){
            if(i.isDigit() || i=='.'){
                current+=i
            }
            else{
                li.add(current.toFloat())
                current=""
                li.add(i)
            }
        }

        if(current!=""){
            li.add(current.toFloat())
        }
        return li
    }
}