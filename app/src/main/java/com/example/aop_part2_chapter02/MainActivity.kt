package com.example.aop_part2_chapter02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    //private형태 경우 findviewbyid 설정시 이렇게.. ... by lazy{} 기존 코드는 버그 생김
    private val clearButton: Button by lazy {
        findViewById(R.id.clearButton)
    }

    private val addButton: Button by lazy {
        findViewById(R.id.addButton)
    }

    private val runButton: Button by lazy {
        findViewById(R.id.runButton)
    }

    private val numberFicker: NumberPicker by lazy {
        findViewById(R.id.numberPicker)
    }

    //리스트에 넣어서 한번에 초기화
    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.textView1),
            findViewById(R.id.textView2),
            findViewById(R.id.textView3),
            findViewById(R.id.textView4),
            findViewById(R.id.textView5),
            findViewById(R.id.textView6),
        )

    }


    //번호를 추가할 수 없는 상태가 있기 때문에 예외 변수 생성
    private var didRun = false

    //중복이 되면 안되기 때문에 중복이 안되는 set 사용 ,, mutableset도 사용 가능
    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//numberFicker 최소숫자 1, 최대숫자 45
        numberFicker.minValue = 1
        numberFicker.maxValue = 45

        initRunButton()
        initaddButton()
        initClearButton()
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            //list데이터를 하나하나 꺼내주는 forEach
            numberTextViewList.forEach {
                it.isVisible = false
            }
        }

    }

    private fun initaddButton() {
        addButton.setOnClickListener {
            //랜덤한 번호를 추가 할 수 없는 상태일때 창 띄움
            if (didRun) {
                Toast.makeText(this, "초기화 후에 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //pickNumberSet 사이즈가 5가 되면 더이상 추가가 안되도록
            if (pickNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //이미 포함되어 있는 번호를 선택한 경우 추가가 안되도록
            if (pickNumberSet.contains(numberFicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberFicker.value.toString()

            pickNumberSet.add(numberFicker.value)

        }
    }

    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandomNumber()
            //forEachIndexed는 인덱스 값도 불러 옴
            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]

                textView.text = number.toString()
                textView.isVisible = true
            }

            didRun = true

            Log.d("MainActivity", list.toString())
        }
    }

    private fun getRandomNumber(): List<Int> {
// numberList에 1부터45까지 int형의 숫자를 넣어준다
        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {

                    if (pickNumberSet.contains(i)) {
                        continue
                    }

                    this.add(i)
                }
            }
        //nuberList 랜덤하게 섞기
        numberList.shuffle()
// 이미 pickNumberSet 선택한 값을 리스트로 변환 + pickNumberSet의 값을 제외 랜덤으로 3개의 번호만 추출
        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)
//넘버 오름차순으로 정렬
        return newList.sorted()
    }
}