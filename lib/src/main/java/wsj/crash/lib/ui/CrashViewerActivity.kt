package wsj.crash.lib.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wsj.crash.lib.R
import wsj.crash.lib.adapter.LogAdapter
import wsj.crash.lib.bean.LogBean
import wsj.crash.lib.db.DbManager

class CrashViewerActivity : AppCompatActivity() {
    lateinit var adapter: LogAdapter
    private val mData = ArrayList<LogBean>()

    private lateinit var rvLog: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_viewer)

        initView()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initView() {
        rvLog = findViewById(R.id.rvLog)
        tvEmpty = findViewById(R.id.tvEmpty)

        adapter = LogAdapter(this, mData)
        adapter.setOnDeleteListener {
            DbManager.getInstance(this).deleteById(it)
            initData()
        }
        rvLog.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvLog.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rvLog.adapter = adapter
    }

    private fun initData() {
        val query = DbManager.getInstance(this).queryAll()
        mData.clear()
        for (hashMap in query) {
            val item = LogBean()
            item.id = hashMap["id"]!!.toInt()
            item.info = hashMap["profile"]
            item.time = hashMap["time"]!!.toLong()
            mData.add(item)
        }
        if (mData.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            rvLog.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            rvLog.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        }
    }
}