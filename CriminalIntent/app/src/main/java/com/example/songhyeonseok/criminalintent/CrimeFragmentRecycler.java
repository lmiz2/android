package com.example.songhyeonseok.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.UUID;

import static com.example.songhyeonseok.criminalintent.CrimeFragmentRecycler.ViewType.ViewType_Null;
//
//public class CrimeFragmentRecycler extends Fragment {
//    public enum ViewType{
//        ViewType_TitleHeader
//        , ViewType_GroupHeader
//        , ViewType_Button
//        , ViewType_CheckBox
//        , ViewType_Null
//
//    }
//
//    public enum CellIndex{
//        CellIndex_TitleHeader("", ViewType.ViewType_TitleHeader)
//        ,CellIndex_DetailsHeader("Detail", ViewType.ViewType_GroupHeader)
//        ,CellIndex_Date("Date", ViewType.ViewType_Button)
//        ,CellIndex_Time("Time", ViewType.ViewType_Button)
//        ,CellIndex_Solved("Solved", ViewType.ViewType_CheckBox)
//        ,CellIndex_ChooseSuspect("ChooseSuspect", ViewType.ViewType_Button)
//        ,CellIndex_SendReport("SendReport", ViewType.ViewType_Button)
//        ,CellIndex_Count("Count", ViewType_Null);
//
//        private final String title;
//        private final ViewType viewType;
//
//
//
//        CellIndex(String title, ViewType viewType) {
//            this.title = title;
//            this.viewType = viewType;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public ViewType getViewType() {
//            return viewType;
//        }
//    }
//
//    private static final String ARG_CRIME_ID = "crime_id";
//    private Crime mCrime;
//    private RecyclerView mRecyclerView;
//
//    public static CrimeFragmentRecycler newInstance(UUID crimeId) {
//
//        Bundle args = new Bundle();
//        args.putSerializable(ARG_CRIME_ID, crimeId);
//        CrimeFragmentRecycler fragment = new CrimeFragmentRecycler();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
//
//        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
//        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_crime_recycler, container, false);
//        mRecyclerView = v.findViewById(R.id.crime_in_recycler_view);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mRecyclerView.setAdapter(new CrimeAdapter());
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    private class ButtonHolder extends RecyclerView.ViewHolder{
//        private Button mButton;
//        public ButtonHolder(View itemView){
//            super(itemView);
//            mButton = (Button)itemView;
//
//        }
//
//        public void BindButton(String title){
//            mButton.setText(title);
//        }
//
//    }
//
//    private class SwitchHolder extends RecyclerView.ViewHolder{
//        private Switch mSwitch;
//
//        public SwitchHolder(View itemView){
//            super(itemView);
//            mSwitch = (Switch) itemView;
//        }
//
//        public void BindSwitch(String title, boolean flag){
//            mSwitch.setText(title);
//            mSwitch.setChecked(flag);
//        }
//    }
//
//    private class CrimeAdapter
//        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//        @Override
//        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {//화면에 아이템에 노출될때마다  실행됨.
//            CellIndex cell = CellIndex.values()[position];
//            switch (cell){
//                case CellIndex_Date: {
//                    ButtonHolder btnholder = (ButtonHolder) holder;
//                    btnholder.BindButton(Crime.getDateString(mCrime.getDate()));
//                }
//                    break;
//                case CellIndex_Solved: {
//                    SwitchHolder switchHolder = (SwitchHolder) holder;
//                    switchHolder.BindSwitch(cell.getTitle(), mCrime.isSolved());
//                }
//                    break;
//                case CellIndex_SendReport: {
//                    ButtonHolder btnholder = (ButtonHolder) holder;
//                    btnholder.BindButton(cell.getTitle());
//                }
//                    break;
//                case CellIndex_ChooseSuspect:
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        @NonNull
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewTypeInt) {//화면에 나오는 사이즈만큼만 뷰홀더를 만들기에, 이것은 화면 크기에 들어갈 아이템 갯수만큼만 호출됨.
//            RecyclerView.ViewHolder holder = null;
//            ViewType viewType = ViewType.values()[viewTypeInt];
//            switch (viewType){
//                case ViewType_Button: {
//                        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//                        View view = layoutInflater.inflate(R.layout.button_cell, parent, false);
//                        View v = view.findViewById(R.id.button_cell_id);
//                        holder = new ButtonHolder(v);
//                    }
//                    break;
//                case ViewType_GroupHeader:
//                    break;
//                case ViewType_TitleHeader:
//                    break;
//                case ViewType_Null:
//                    break;
//                case ViewType_CheckBox: {
//                    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//                    View view = layoutInflater.inflate(R.layout.switch_cell, parent, false);
//                    View v = view.findViewById(R.id.switch_cell_id);
//                    holder = new ButtonHolder(v);
//                }
//                    break;
//                default:
//                    break;
//            }
//            return holder;
//        }
//
//        @Override
//        public int getItemCount() {
//            return CellIndex.CellIndex_Count.ordinal();//ordinal() --> 지수?상수?
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            CellIndex cellIndex = CellIndex.values()[position];
//            int viewType = cellIndex.getViewType().ordinal();//ordinal --> 정수로 바꾸기?
//            return viewType;
//        }
//    }
//}

public class CrimeFragmentRecycler extends Fragment {
    public static final int REQUEST_DATE = 3153;
    public static final int REQUEST_TIME = 3154;

    public static final String DIALOG_DATE = "dateDIAL";

    public enum ViewType {

        ViewType_TitleHeader

        , ViewType_GroupHeader

        , ViewType_Button

        , ViewType_Checkbox

        , ViewType_Null

    }

    public enum CellIndex {
        // photo, camera    Title header, title edit
        CellIndex_TitleHeader("", ViewType.ViewType_TitleHeader)
        , CellIndex_DetailsHeader("Detail", ViewType.ViewType_GroupHeader) // details & bottom border
        , CellIndex_Date("Date", ViewType.ViewType_Button)
        , CellIndex_Time("Time", ViewType.ViewType_Button)
        , CellIndex_Solved("Solved", ViewType.ViewType_Checkbox)    // checkbox
        , CellIndex_ChooseSuspect("Choose Suspect", ViewType.ViewType_Button)
        , CellIndex_SendReport("Send Crime Report", ViewType.ViewType_Button)
        , CellIndex_Count("", ViewType.ViewType_Null)
        ;

        private final String title;
        private final ViewType viewType;

        CellIndex(String title, ViewType viewType) {
            this.title = title;
            this.viewType = viewType;
        }

        public String getTitle() {
            return title;
        }

        public ViewType getViewType() {
            return viewType;
        }
    }

    private static final String ARG_CRIME_ID = "crime_id";

    private Crime mCrime;
    private RecyclerView mRecyclerView;

    public static CrimeFragmentRecycler newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragmentRecycler fragment = new CrimeFragmentRecycler();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_recycler, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_crime_recycler_widget);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new CrimeAdapter());

        return v;
    }

    // ViewHolders
    private class ButtonHolder extends RecyclerView.ViewHolder {
        private Button mButton;

        public ButtonHolder(View itemView) {
            super(itemView);
            mButton = (Button)itemView;
        }
        public Button BindButton(String title) {
            mButton.setText(title);
            return mButton;
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        private ImageView mPhoto;
        private ImageButton mCamera;
//        private EditText mTitleEdit;

        public HeaderHolder(View itemView) {
            super(itemView);
            View v = itemView.findViewById(R.id.crime_header_cell_id);
            mPhoto = v.findViewById(R.id.crime_photo);
            mCamera = v.findViewById(R.id.crime_camera);
        }
        public void BindHeader(String title) {
//            mTitleEdit.setText(title);
//            mCamera.setBackground();
        }
    }

    private class SwitchHolder extends RecyclerView.ViewHolder {
        private Switch mButton;

        public SwitchHolder(View itemView) {
            super(itemView);
            mButton = (Switch)itemView;
        }
        public Switch BindSwitch(String title, boolean checked) {
            mButton.setText(title);
            mButton.setChecked(checked);
            return mButton;
        }
    }

    private class GroupHeaderHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public GroupHeaderHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }
        public TextView BindText(String title) {
            mTextView.setText(title);

            return mTextView;
        }
    }

    // ViewAdaptor
    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
            CellIndex cell = CellIndex.values()[i];
            switch (cell) {
                case CellIndex_TitleHeader:{
                    HeaderHolder h = (HeaderHolder)holder;
                    h.BindHeader(cell.getTitle());
                }
                break;
                case CellIndex_DetailsHeader:{
                    GroupHeaderHolder gh = (GroupHeaderHolder)holder;
                    gh.BindText(cell.getTitle());
                }
                break;
                case CellIndex_Date: {
                    ButtonHolder buttonHolder = (ButtonHolder)holder;
                    Button button = buttonHolder.BindButton(Crime.getDateString(mCrime.getDate()));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager manager = getFragmentManager();
                            DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                            dialog.setTargetFragment(CrimeFragmentRecycler.this, REQUEST_DATE);
                            dialog.show(manager, DIALOG_DATE);
                        }
                    });

                }
                break;
                case CellIndex_Time: {
                    ButtonHolder buttonHolder = (ButtonHolder)holder;
                    Button button = buttonHolder.BindButton(Crime.getTimeString(mCrime.getDate()));
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });
                }
                break;
                case CellIndex_ChooseSuspect:
                case CellIndex_SendReport: {
                    ButtonHolder buttonHolder = (ButtonHolder)holder;
                    buttonHolder.BindButton(cell.getTitle());
                }
                break;
                case CellIndex_Solved: {
                    SwitchHolder switchHolder = (SwitchHolder)holder;
                    switchHolder.BindSwitch(cell.getTitle(), mCrime.isSolved());
                }
                break;
                default:
                    break;
            }

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            RecyclerView.ViewHolder holder = null;
            ViewType viewType = ViewType.values()[i];
            switch (viewType) {
                case ViewType_Button: {
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    View view = layoutInflater
                            .inflate(R.layout.button_cell, viewGroup, false);
                    View v = view.findViewById(R.id.button_cell_id);
                    holder = new ButtonHolder(v);
                }
                break;
                case ViewType_Checkbox: {
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    View view = layoutInflater
                            .inflate(R.layout.switch_cell, viewGroup, false);
                    View v = view.findViewById(R.id.switch_cell_id);
                    holder = new SwitchHolder(v);
                }
                break;
                case ViewType_TitleHeader:{
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    View view = layoutInflater
                            .inflate(R.layout.crime_header_cell, viewGroup, false);
                    View v = view.findViewById(R.id.crime_header_cell_id);
                    holder = new HeaderHolder(v);
                }
                break;
                case ViewType_GroupHeader:{
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    View view = layoutInflater
                            .inflate(R.layout.group_header_cell, viewGroup, false);
                    View v = view.findViewById(R.id.group_header_cell_id);
                    holder = new GroupHeaderHolder(v);
                }
                break;
                default: {
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    View view = layoutInflater
                            .inflate(R.layout.button_cell, viewGroup, false);
                    View v = view.findViewById(R.id.button_cell_id);
                    holder = new ButtonHolder(v);
                }
                break;
            }

            return holder;
        }

        @Override
        public int getItemCount() {
            return CellIndex.CellIndex_Count.ordinal();
        }

        @Override
        public int getItemViewType(int position) {
            CellIndex cellIndex = CellIndex.values()[position];
            return cellIndex.getViewType().ordinal();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        switch (requestCode){
            case REQUEST_DATE :
                if(data == null){
                    return;
                }

                break;
            case REQUEST_TIME :
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
