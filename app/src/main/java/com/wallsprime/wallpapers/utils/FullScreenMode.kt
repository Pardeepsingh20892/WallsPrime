package com.wallsprime.wallpapers.utils


/*


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI() else showSystemUI()
    }


*/


/*


    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Tell the window that we want to handle/fit any system windows
            WindowCompat.setDecorFitsSystemWindows(window, false)

            val controller = binding.root.windowInsetsController

            // Hide the keyboard (IME)
            controller?.hide(WindowInsets.Type.ime())

            // Sticky Immersive is now ...
            controller?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            // When we want to hide the system bars
            controller?.hide(WindowInsets.Type.systemBars())

            */
/*val flag = WindowInsets.Type.statusBars()
            WindowInsets.Type.navigationBars()
            WindowInsets.Type.captionBar()
            window?.insetsController?.hide(flag)*//*

        } else {
            //noinspection
            @Suppress("DEPRECATION")
            // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }



*/



/*



private fun hideSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, binding.root).let { controller ->
        controller.hide(WindowInsetsCompat.Type.statusBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

private fun showSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(window, binding.root).show(WindowInsetsCompat.Type.systemBars())
}



*/




/*

 private fun setFullScreen() {
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
         window.setDecorFitsSystemWindows(false)
         val controller = window.insetsController
         if (controller != null) {
             controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
             controller.systemBarsBehavior =
                 WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
         }
     } else {
         window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                 or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                 or View.SYSTEM_UI_FLAG_IMMERSIVE
                 or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                 or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                 or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
     }
 }

*/



//<item name="android:windowLayoutInDisplayCutoutMode">shortEdges</item>
