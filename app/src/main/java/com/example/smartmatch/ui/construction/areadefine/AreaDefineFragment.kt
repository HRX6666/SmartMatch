package com.example.smartmatch.ui.construction.areadefine

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.base.util.StatusUtil
import com.example.smartmatch.base.activity.BaseFragment
import com.example.smartmatch.base.kxt.toast
import com.example.smartmatch.base.util.safeLaunch
import com.example.smartmatch.databinding.FragmentAreaDefineBinding
import com.example.smartmatch.logic.model.MMNetResponse
import com.example.smartmatch.logic.model.MmnetData
import com.example.smartmatch.logic.network.model.ResponseMessage
import com.example.smartmatch.ui.construction.ConstructionListener
import com.example.smartmatch.ui.findC.FindCActivity
import com.example.smartmatch.ui.view.ItemButton
import com.kongzue.dialogx.dialogs.InputDialog

/**
 * @className AreaDefineFragment
 * @description 场景定义
 * @author Voyager
 * @date 2023/10/2 19:09
 */

class AreaDefineFragment : BaseFragment<FragmentAreaDefineBinding>(), ConstructionListener {
    /**
     * area的view数组的最后元素的index，便于删除view
     */
    private var lastIndex = 0
    val NET_ID="net_id"

    private val mViewModel: AreaDefineViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[AreaDefineViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun FragmentAreaDefineBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.constructionListener = this@AreaDefineFragment
        viewLifecycleOwner.safeLaunch {
            if (mViewModel.mmnetData == null)
                mViewModel.getMMNetData()
        }
        initListener()
        StatusUtil.initFragmentBar(this@AreaDefineFragment, false)
    }

    override fun initListener() {
        super.initListener()
        setVisibilityListener(binding.arrowButtonC, binding.containerC)
        setVisibilityListener(binding.arrowButtonArea, binding.containerArea)
    }

    private fun setVisibilityListener(button: CompoundButton, container: View) {
        button.setOnClickListener {
            container.visibility = if (button.isChecked) View.VISIBLE else View.GONE
        }
    }

    override fun initRecyclerList(mmnet_data: List<MmnetData>) {
        super.initRecyclerList(mmnet_data)
        val context = requireContext()

        binding.containerArea.removeAllViews()
        binding.containerC.removeAllViews()

        for (i in mmnet_data.indices) {
            val name = mmnet_data[i].mmnet_name

            val net = createItemButton(context, name) {
                lastIndex = 0
                binding.searchControl.setText(name)
                binding.containerArea.removeAllViews()

                for (j in mmnet_data[i].areas.areas_data.indices) {
                    val areaName = mmnet_data[i].areas.areas_data[j].area.name
                    val area = createItemButton(context, areaName) {
                        Toast.makeText(context, "Clicked on: $areaName", Toast.LENGTH_SHORT).show()
                    }
                    requireActivity().runOnUiThread{
                        binding.containerArea.addView(area)
                    }

                    lastIndex++
                }

                val newArea = createItemButton(context, "新建区域") {
                    InputDialog("新建区域", "请输入区域名称", "确定", "取消", "")
                        .setCancelable(true)
                        .setOkButton { baseDialog, v, inputStr ->
                            requireActivity().toast("输入的内容：$inputStr")
                            addNewView(inputStr)
                                //TODO 跳转
                            val intent=Intent(requireActivity(),FindCActivity::class.java)
                              intent.putExtra(NET_ID,mmnet_data[i].mmnet_id)//发送mmnet的id
                            requireActivity().startActivity(intent)
                          //  mViewModel.createNewArea(mmnet_data[i].mmnet_id, inputStr)
                            false
                        }
                        .show()
                }
                requireActivity().runOnUiThread{
                    binding.containerArea.addView(newArea)
                }
            }
            requireActivity().runOnUiThread{
                binding.containerC.addView(net)
            }
        }
    }

    private inline fun createItemButton(
        context: Context,
        text: String,
        crossinline onClick: () -> Unit
    ): ItemButton {
        val button = ItemButton(context, null)
        button.text(text)
        button.setOnClickListener { onClick() }
        return button
    }

    override fun addNewView(name: String) {
        super.addNewView(name)
        val area = ItemButton(requireContext(), null)
        area.text(name)
        area.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked on", Toast.LENGTH_SHORT)
                .show()
        }
        binding.containerArea.addView(area, lastIndex)
        lastIndex++
    }

    override fun processMMNetData(result: LiveData<Result<MMNetResponse>>) {
        super.processMMNetData(result)
        result.observe(this) { re ->
            val response = re.getOrNull()
            if (response != null) {
                val data = response.data
                val mmnetDataList = data.mmnet_data
                initRecyclerList(mmnetDataList)
            }

        }
    }

    override fun processResponse(result: LiveData<Result<ResponseMessage>>) {
        super.processResponse(result)
        result.observe(this) { re ->
            val responseMsg=re.getOrNull()
            responseMsg?.name?.isEmpty()
        }
    }
}