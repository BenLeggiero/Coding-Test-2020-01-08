package me.benleggiero.codingtest2020_01_08.conveniences

import android.os.AsyncTask


fun <Argument, Result> async(
    argument: Argument,
    backgroundFunction: (argument: Argument) -> Result,
    onComplete: (result: Result) -> Unit = { }
) {
    object : AsyncTask<Argument, Unit, Result>() {
        override fun doInBackground(vararg _unused: Argument): Result {
            return backgroundFunction(argument)
        }


        override fun onPostExecute(result: Result) {
            super.onPostExecute(result)
            onComplete(result)
        }
    }
        .execute()
}


fun <Result> async(
    backgroundFunction: (Unit) -> Result,
    onComplete: (result: Result) -> Unit
) {
    return async(Unit, backgroundFunction, onComplete)
}


fun async(backgroundFunction: (Unit) -> Unit) {
    return async(backgroundFunction = backgroundFunction, onComplete = { })
}
