package com.phat.testbase.devphat.extensions.module


@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject(val singleton: Boolean = false)
