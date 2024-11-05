package com.example.sqllitekotlinexample
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val userList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var onItemClickListener: ((User) -> Unit)? = null

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.usernameTextView)
        val password: TextView = view.findViewById(R.id.passwordTextView)
        val photo: ImageView = view.findViewById(R.id.userPhotoImageView)
        val cardView: CardView = view.findViewById(R.id.CardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.username.text = user.username
        holder.password.text = user.password
        holder.photo.setImageResource(user.photo)
        holder.cardView.setOnClickListener {
            onItemClickListener?.invoke(user)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setOnItemClickListener(listener: (User) -> Unit) {
        onItemClickListener = listener
    }
}
