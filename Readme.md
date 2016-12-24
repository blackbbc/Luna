# Luna
Android instance state helper. It uses apt to generate codes.

## Download

```
TODO
```

## Usage

Use `@State` annotation to annotate variables that need to save.
``` Java
public class MainActivity extends AppCompatActivity {
    @State int testInt;
    @State Integer testInteger;
    @State boolean testBoolean;
    @State float testFloat;
    @State String testString;
    @State ArrayList<Integer> integers;
    @State List<String> strings;
    @State List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
```

Run "Build" to generate necessary helper codes. The helper codes is named by prefix `Luna` + `ClassName`. For example, the above `MainActivity` will generate `LunaMainActivity` class. Then add the following codes to use them!.

```Java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    LunaMainActivity.onRestoreInstanceState(this, savedInstanceState);
}

@Override
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    LunaMainActivity.onSaveInstanceState(this, outState);
}
```

That's all!

## Support types:
1. Boxed and unboxed primitive type.
  - `boolean`, `byte`, `short`, `int`, `long`, `char`, `float`, `double`
  - `Boolean`, `Byte`, `Short`, `Int`, `Long`, `Char`, `Float`, `Double`
2. `String`
3. `Parcelable`
4. `List<Integer>`, `List<String>`, `List<Parcelable>`
