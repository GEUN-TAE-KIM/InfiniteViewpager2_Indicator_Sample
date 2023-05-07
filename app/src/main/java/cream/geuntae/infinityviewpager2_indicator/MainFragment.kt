package cream.geuntae.infinityviewpager2_indicator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import cream.geuntae.infinityviewpager2_indicator.adapter.ImageAdapter
import cream.geuntae.infinityviewpager2_indicator.adapter.TextAdapter
import cream.geuntae.infinityviewpager2_indicator.data.Texts
import cream.geuntae.infinityviewpager2_indicator.databinding.FragmentMainBinding
import java.lang.Math.abs

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTop()
    }

    private fun setTop() {

        val choonsik: Array<Int> = arrayOf(
            R.drawable.choonsik1,
            R.drawable.choonsik2,
            R.drawable.choonsik3,
            R.drawable.choonsik4,
            R.drawable.choonsik5,
            R.drawable.choonsik6)


        with(binding.viewpagerHomeBanner) {
            adapter = ImageAdapter(choonsik).apply {

                binding.viewpagerHomeBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                //  setTopText()
            }

            val pageWidth = resources.getDimension(R.dimen.dimen_310)
            val pageMargin = resources.getDimension(R.dimen.dimen_15)
            val screenWidth = resources.displayMetrics.widthPixels
            val offset = screenWidth - pageWidth - pageMargin

            binding.viewpagerHomeBanner.offscreenPageLimit = 2

            binding.viewpagerHomeBanner.setPageTransformer { page, position ->
                page.translationX = position * -offset

            }

            // 이미지 양 옆에 슬라이드를 보이게 하기 위한 이미지 곂치기 / 자르기 설정
            val offsetBetweenPages = resources.getDimensionPixelOffset(R.dimen.dimen_30).toFloat()
            binding.viewpagerHomeBanner.setPageTransformer { page, position ->
                val myOffset = position * -(1.40f * offsetBetweenPages)
                if (position < -1) {
                    page.translationX = -myOffset
                } else if (position <= 1) {

                    val scaleFactor = 1.02f.coerceAtLeast(1 - abs(position))
                    page.translationX = myOffset
                    page.scaleY = scaleFactor
                    page.alpha = scaleFactor
                } else {
                    page.alpha = 0f
                    page.translationX = myOffset
                }
            }

            // 자동 스크롤 시간 설정
            binding.viewpagerHomeBanner.postDelayed({ autoScroll(binding.viewpagerHomeBanner) },
                7000)

            // 이미지와 연동을 위해 여기에 설정
            setTopText()
            setupOnBoardingIndicators()
            setCurrentBannerIndicator(0)

            // 초기 탭 위치
            // currentPage = 50 / 2 - ceil(choonsik.size.toDouble() / 2).toInt()
            currentPage = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % choonsik.size
            binding.viewpagerHomeBanner.setCurrentItem(currentPage, false)

            // 콜백을 함으로서 해당 이미지의 위치에 맞게 텍스트와 인디케이터를 설정함
            binding.viewpagerHomeBanner.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    currentPage = position
                    setCurrentBannerIndicator(position)
                    binding.textView.setCurrentItem(position, false)

                    if (position == binding.viewpagerHomeBanner.adapter!!.itemCount - 1) {
                        binding.viewpagerHomeBanner.setCurrentItem(0, false)
                        setCurrentBannerIndicator(position)
                    }

                    // 뷰모델로 불러올떄
                    /*if (position == 0) {
                        binding.viewpagerHomeBanner.setCurrentItem(500, false)
                        setCurrentBannerIndicator(position)
                    } else if (position == Int.MAX_VALUE - 1) {
                        binding.viewpagerHomeBanner.setCurrentItem(500, false)
                        setCurrentBannerIndicator(position)
                    }*/

                }
            })

            binding.leftButton.setOnClickListener {
                binding.viewpagerHomeBanner.currentItem = currentPage - 1
            }

            binding.rightButton.setOnClickListener {
                binding.viewpagerHomeBanner.currentItem = currentPage + 1
            }


        }
    }

    private fun setTopText() {

        val items = listOf(
            Texts("춘식과 라이언1"),
            Texts("춘식과 라이언2"),
            Texts("춘식과 라이언3"),
            Texts("춘식과 라이언4"),
            Texts("춘식과 라이언5"),
            Texts("춘식과 라이언6")
        )

        with(binding.textView) {
            adapter = TextAdapter(items).apply {

                binding.textView.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }

            //   binding.mainTextRecyclerView.offscreenPageLimit = 3

            currentPage = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % items.size
            binding.textView.setCurrentItem(currentPage, false)


            binding.textView.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    currentPage = position

                }
            })

        }
    }

    // 탭레이아웃 인디케이터로 하면은 무한 스크롤을 하면 해당 아이템 만큼 늘어나게 되서 코드에서 구현을 해야 함
    private fun setupOnBoardingIndicators() {

        // 해당 거는 뷰모델로 이미지를 불러와서 해당 뷰를 갱신을 할때 새롭게 불리면서 갯수가 늘어나는 현상이 발생해서 불러올때 다시 다 제거를 해서 새롭게 불리게 하면 됨
        binding.indicators2.removeAllViews()

        // 샘플은 내부로 지정해서 했지만 데이터 받아서 할떄는 아이템 갯수를 받아서 하면 됨
        val indicators = arrayOfNulls<ImageView>(6)

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        layoutParams.leftMargin = 30

        for (i in indicators.indices) {
            indicators[i] = ImageView(requireActivity().applicationContext)
            indicators[i]?.setImageDrawable(ContextCompat.getDrawable(
                requireActivity().applicationContext,
                R.drawable.arrow_indicator_inactivie
            ))

            indicators[i]?.layoutParams = layoutParams

            binding.indicators2.addView(indicators[i])
        }
    }

    private fun setCurrentBannerIndicator(index: Int) {

        val childCount = binding.indicators2.childCount
        val position = index % childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicators2.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(activity?.let {
                    ContextCompat.getDrawable(requireActivity().applicationContext,
                        R.drawable.arrow_indicator_active)
                })
            } else {
                imageView.setImageDrawable(activity?.let {
                    ContextCompat.getDrawable(requireActivity().applicationContext,
                        R.drawable.arrow_indicator_inactivie)
                })
            }
        }
    }

    private fun autoScroll(viewPager: ViewPager2) {
        val currentPage = viewPager.currentItem
        val totalPages = viewPager.adapter?.itemCount ?: 0

        viewPager.setCurrentItem((currentPage + 1) % totalPages, true)
        viewPager.postDelayed({ autoScroll(viewPager) }, 8000)
    }

}