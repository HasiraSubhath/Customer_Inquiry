package com.kotlin.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.mad.adapters.BillAdapter
import com.kotlin.mad.models.BillModel
import com.kotlin.mad.R
import com.google.firebase.database.*

class BillFetchingActivity : AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var billList: ArrayList<BillModel>
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        billList = arrayListOf<BillModel>()

        getBillData()


    }

    private fun getBillData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("InquiryDB")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               billList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val billData = empSnap.getValue(BillModel::class.java)
                        billList.add(billData!!)
                    }
                    val mAdapter = BillAdapter(billList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : BillAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@BillFetchingActivity, BillDetailsActivity::class.java)

                            //put extra(passing data to another activity)
                            intent.putExtra("cId", billList[position].cId)
                            intent.putExtra("cName", billList[position].cName)
                            intent.putExtra("cNumber", billList[position].cNumber)
                            intent.putExtra("cType", billList[position].cType)
                            intent.putExtra("cInquiry", billList[position].cInquiry)
                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}