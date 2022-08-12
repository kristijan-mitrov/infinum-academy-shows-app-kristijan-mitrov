package shows.kristijanmitrov.infinumacademyshows

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import shows.kristijanmitrov.infinumacademyshows.databinding.RatingBarViewBinding

class RatingBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: RatingBarViewBinding

    init {
        binding = RatingBarViewBinding.inflate(LayoutInflater.from(context), this)

        clipChildren = false
        clipToPadding = false

        setPadding(
            context.resources.getDimensionPixelSize(R.dimen.spacing_2x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_1x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_2x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_1x)
        )

        val a = context.obtainStyledAttributes(attrs, R.styleable.RatingBarView)

        val rating = a.getFloat(R.styleable.RatingBarView_rating, 0f)
        val size = a.getString(R.styleable.RatingBarView_size)
        setRating(rating)
        setSize(size)
        a.recycle()

    }

    private fun setSize(size: String?) = with(binding) {

        when (size) {
            "small" -> {
                imageView1.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.small)
                imageView1.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.small)
                imageView2.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.small)
                imageView2.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.small)
                imageView3.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.small)
                imageView3.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.small)
                imageView4.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.small)
                imageView4.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.small)
                imageView5.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.small)
                imageView5.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.small)
            }
            "large" -> {
                imageView1.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.large)
                imageView1.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.large)
                imageView2.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.large)
                imageView2.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.large)
                imageView3.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.large)
                imageView3.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.large)
                imageView4.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.large)
                imageView4.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.large)
                imageView5.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.large)
                imageView5.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.large)
            }
            else -> {
                imageView1.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.medium)
                imageView1.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.medium)
                imageView2.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.medium)
                imageView2.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.medium)
                imageView3.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.medium)
                imageView3.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.medium)
                imageView4.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.medium)
                imageView4.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.medium)
                imageView5.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.medium)
                imageView5.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.medium)
            }
        }

    }

    fun setRating(rating: Float) = with(binding) {

        if (rating <= 1f) {
            imageView1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
            imageView3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
            imageView4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
            imageView5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
        } else if (rating <= 1.5f) {
            imageView1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_half))
            imageView3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
            imageView4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
            imageView5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
        } else if (rating <= 2f) {
            imageView1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
            imageView4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
            imageView5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
        } else if (rating <= 2.5f) {
            imageView1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_half))
            imageView4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
            imageView5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
        } else if (rating <= 3f) {
            imageView1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
            imageView5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
        } else if (rating <= 3.5f) {
            imageView1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_half))
            imageView5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
        } else if (rating <= 4f) {
            imageView1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_empty))
        } else if (rating <= 4.5f) {
            imageView1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_half))
        } else {
            imageView1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
            imageView5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_full))
        }

    }
}