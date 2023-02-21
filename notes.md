# Development Notes

## Navigation

*Don't* use `getFragmentManager` or similar to change/replace fragments.
Use the [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started) for navigation. You'll need to edit `nav_graph.xml`

## Layouts

- Default to Fragments when possible. Only create a new Activity when absolutely needed.
- Use `androidx.constraintlayout.widget.ConstraintLayout` when possible. Avoid nesting `LinearLayouts` or similar.
- Use Material components `com.google.android.material.*` if available to keep a consistent theme for the app.

### Enabling scrolling on soft input
When using a soft input (i.e. on-screen keyboard), it can sometimes cover the input. To allow the activity to resize, add `android:windowSoftInputMode="adjustResize"` in the `<activity>` tag in `AndroidManifest.xml` (already done in this project). And in the fragment with the input, make sure that the `ConstraintLayout` contains `android:fitsSystemWindows="true"`.

