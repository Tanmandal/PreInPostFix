package com.example.preinpostfix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    EditText inp;
    TextView pre,in,post;
    RadioButton bpre,bin,bpost;
    Button go,clr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUIviews();
        bpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearview();
                bin.setChecked(false);
                bpost.setChecked(false);
            }
        });
        bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearview();
                bpre.setChecked(false);
                bpost.setChecked(false);
            }
        });
        bpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearview();
                bin.setChecked(false);
                bpre.setChecked(false);
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=otherValid(inp.getText().toString()),prf="",inf="",pof="";
                closeKeyboard();
                if(bpre.isChecked())
                {
                    PreFix p=new PreFix();
                    InFix i=new InFix();
                    inf=p.pretoin(s);
                    if(inf.equals("INVALID")) {
                        prf = "INVALID";
                        pof="INVALID";
                    }
                    else {
                        prf = s;
                        pof=i.intopost(inf);
                    }
                }
                else if(bpost.isChecked())
                {
                    PostFix p=new PostFix();
                    InFix i=new InFix();
                    inf=p.posttoin(s);
                    if(inf.equals("INVALID")) {
                        prf = "INVALID";
                        pof="INVALID";
                    }
                    else {
                        pof = s;
                        prf=i.intopre(inf);
                    }
                }
                else if(bin.isChecked())
                {
                    InFix i=new InFix();
                    prf=i.intopre(s);
                    pof=i.intopost(s);
                    if(prf.equals("INVALID"))
                        inf=prf;
                    else
                        inf=s;
                }
                pre.setText(prf);
                in.setText(inf);
                post.setText(pof);
            }
        });
        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearview();
                inp.setText("");
            }
        });
        in.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String s=in.getText().toString();
                if(s.equals("INVALID") || s.length()==0)
                    Toast.makeText(getApplicationContext(), "Nothing to Copy",Toast.LENGTH_LONG).show();
                else
                {
                    copy(s);
                    Toast.makeText(getApplicationContext(), "InFix Copied",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        pre.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String s=pre.getText().toString();
                if(s.equals("INVALID") || s.length()==0)
                    Toast.makeText(getApplicationContext(), "Nothing to Copy",Toast.LENGTH_LONG).show();
                else
                {
                    copy(s);
                    Toast.makeText(getApplicationContext(), "PreFix Copied",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        post.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String s=post.getText().toString();
                if(s.equals("INVALID") || s.length()==0)
                    Toast.makeText(getApplicationContext(), "Nothing to Copy",Toast.LENGTH_LONG).show();
                else
                {
                    copy(s);
                    Toast.makeText(getApplicationContext(), "PostFix Copied",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        inp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(in.getText().toString().length()!=0)
                    clearview();
                if (s.length() > 0) {
                    int i=s.toString().indexOf("\n");
                    if (i!=-1) {
                        inp.setText(s.toString().substring(0, i)+s.toString().substring(i+1));
                        inp.setSelection(inp.getText().length());
                        go.performClick();
                    }
                }

            }
        });

    }
    private void setupUIviews()
    {
        inp=(EditText)findViewById(R.id.Inptxt);
        pre=(TextView)findViewById(R.id.Preview);
        in=(TextView)findViewById(R.id.Inview);
        post=(TextView)findViewById(R.id.Postview);
        bpre=(RadioButton)findViewById(R.id.radioPre);
        bin=(RadioButton)findViewById(R.id.radioIn);
        bpost=(RadioButton)findViewById(R.id.radioPost);
        go=(Button)findViewById(R.id.g);
        clr=(Button)findViewById(R.id.cl);
        bin.setChecked(true);
        pre.setMovementMethod(new ScrollingMovementMethod());
        in.setMovementMethod(new ScrollingMovementMethod());
        post.setMovementMethod(new ScrollingMovementMethod());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

    }
    void copy(String s)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("",s);
        clipboard.setPrimaryClip(clip);
    }
    void clearview()
    {
        post.setText("");
        in.setText("");
        pre.setText("");
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }
    String otherValid(String s)
    {
        String t="";
        for(int i=0;i<s.length();i++)
        {
            char ch=s.charAt(i);
            if(ch==' ')
                continue;
            if(ch==247 || ch=='\\')
                t=t+"/";
            else if(ch==215)
                t=t+"*";
            else
                t=t+ch;
        }
        return t;
    }
}
class PreFix
{
    Node top;
    String pretoin(String s)
    {
        pull(s);
        Node temp=top;
        int c=0;
        try
        {
            while(temp.next!=null)
            {
                String ch=temp.next.next.data;
                if("+-*/^".indexOf(ch)!=-1)
                {
                    String k1=temp.next.data;
                    String k2=temp.data;
                    insert("("+k1+ch+k2+")",c);
                    temp=top;c=0;
                }
                else
                {
                    temp=temp.next;
                    c++;
                }
            }
        }catch(Exception e){return "INVALID";}
        return temp.data;
    }
    int countNodes()
    {
        Node temp=top;
        int c=0;
        while(temp!=null)
        {
            c++;
            temp=temp.next;
        }
        return c;
    }
    void insert(String s,int c)
    {
        int p=0;
        Node a=new Node(s);
        if(p==c)
        {
            if(countNodes()>3)
                a.next=top.next.next.next;
            top=a;
        }
        else
        {
            Node temp=top;
            p++;
            while(temp!=null)
            {
                if(p==c)
                {
                    a.next=temp.next.next.next.next;
                    temp.next=a;
                    break;
                }
                p++;
                temp=temp.next;
            }
        }
    }
    void pull(String s)
    {
        top=null;
        for(int i=0;i<s.length();i++)
            push(s.charAt(i));
    }
    void push(char s)
    {
        Node a=new Node(s+"");
        a.next=top;
        top=a;
    }
}
class PostFix
{
    Node rear,front;
    String posttoin(String s)
    {
        pull(s);
        Node temp=front;
        int c=0;
        try
        {
            while(temp.next!=null)
            {
                String ch=temp.next.next.data;
                if("+-*/^".indexOf(ch)!=-1)
                {
                    String k1=temp.next.data;
                    String k2=temp.data;
                    insert("("+k2+ch+k1+")",c);
                    temp=front;c=0;
                }
                else
                {
                    temp=temp.next;
                    c++;
                }
            }
        }catch(Exception e){return "INVALID";}
        return temp.data;
    }
    int countNodes()
    {
        Node temp=front;
        int c=0;
        while(temp!=null)
        {
            c++;
            temp=temp.next;
        }
        return c;
    }
    void insert(String s,int c)
    {
        int p=0;
        Node a=new Node(s);
        if(p==c)
        {
            if(countNodes()>3)
                a.next=front.next.next.next;
            front=a;
        }
        else
        {
            Node temp=front;
            p++;
            while(temp!=null)
            {
                if(p==c)
                {
                    a.next=temp.next.next.next.next;
                    temp.next=a;
                    break;
                }
                p++;
                temp=temp.next;
            }
        }
    }
    void pull(String s)
    {
        rear=null;front=null;
        for(int i=0;i<s.length();i++)
            push(s.charAt(i));
    }
    void push(char s)
    {
        Node a=new Node(s+"");
        if(front==null)
        {
            rear=a; front=rear;
        }
        else
        {
            rear.next=a;
            rear=rear.next;
        }
    }
}
class Node
{
    String data;
    Node next;
    Node()
    {
        data="";
        next=null;
    }
    Node(String s)
    {
        data=s;
        next=null;
    }
}
class InFix
{
    String plusminus(String s,boolean p)
    {
        if(s.length()==1) {
            if("+-*/^".indexOf(s)!=-1)
                throw new ArithmeticException();
            return s;
        }
        else if(s.indexOf("+")==-1 && s.indexOf("-")==-1)
            return muldiv(s,p);
        StringTokenizer t1=new StringTokenizer(s,"+-",true);
        String s1="",s2="",s3="";
        while(t1.hasMoreTokens())
        {
            if(s1.length()==0)
            {
                s1=t1.nextToken();
                s1=muldiv(s1,p);
            }
            s2=t1.nextToken();
            s3=t1.nextToken();
            s3=muldiv(s3,p);
            if(p)
                s1=s2+s1+s3;
            else
                s1=s1+s3+s2;
        }
        return s1;
    }
    String muldiv(String s,boolean p)
    {
        if(s.length()==1) {
            if("+-*/^".indexOf(s)!=-1)
                throw new ArithmeticException();
            return s;
        }
        else if(s.indexOf("*")==-1 && s.indexOf("/")==-1 && s.indexOf("^")==-1)
            return s;
        StringTokenizer t1=new StringTokenizer(s,"*/^",true);
        String s1="",s2="",s3="";
        while(t1.hasMoreTokens())
        {
            if(s1.length()==0)
                s1=t1.nextToken();
            s2=t1.nextToken();
            s3=t1.nextToken();
            if(p)
                s1=s2+s1+s3;
            else
                s1=s1+s3+s2;
        }
        return s1;
    }
    String bracket(String s,boolean p)
    {
        int f=-1,l=-1;
        s=s.trim()+" ";
        for(int i=0;i<s.length();i++)
        {
            char ch=s.charAt(i);
            if(ch=='(')
                f=i;
            else if(ch==')')
            {
                l=i;
                break;
            }
        }
        if(f==-1 && l==-1)
            return s;
        else if(f!=-1 && l!=-1)
        {
            String k=plusminus(s.substring(f+1,l),p);
            s=s.substring(0,f)+alter(k,true)+s.substring(l+1);
        }
        else
        {
            throw new ArithmeticException();
        }
        return bracket(s,p);
    }
    String intopre(String s)
    {
        try{
            check(s);
            s="("+s+")";
            s=bracket(s,true);
            s=alter(s,false);
            return s;
        }catch(Exception e){return "INVALID";}
    }
    String intopost(String s)
    {
        try{
            check(s);
            s="("+s+")";
            s=bracket(s,false);
            s=alter(s,false);
            return s;
        }catch(Exception e){return "INVALID";}
    }
    String alter(String s,boolean k)
    {
        String o1="+-*/^",o2=""+(char)156+(char)157+(char)158+(char)159+(char)160,t="";
        for(int i=0;i<s.length();i++)
        {
            char ch=s.charAt(i);
            int in;
            if(k)
            {
                in=o1.indexOf(ch);
                if(in!=-1)
                    ch=o2.charAt(in);
            }
            else
            {
                in=o2.indexOf(ch);
                if(in!=-1)
                    ch=o1.charAt(in);
            }
            t=t+ch;
        }
        return t;
    }
    void check(String s)
    {
        if(s.length()==0)
            return;
        char prv=s.charAt(0);
        for(int i=1;i<s.length();i++)
        {
            char ch=s.charAt(i);
            if(prv=='(' || prv==')' || ch=='(' || ch==')' || "+-*/^".indexOf(ch)!=-1 || "+-*/^".indexOf(prv)!=-1)
                prv=ch;
            else
                throw new ArithmeticException();
        }
    }
}
