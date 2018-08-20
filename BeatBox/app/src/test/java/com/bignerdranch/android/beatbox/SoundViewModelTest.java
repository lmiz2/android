package com.bignerdranch.android.beatbox;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SoundViewModelTest {
    private BeatBox mBeatBox;
    private Sound mSound;
    private SoundViewModel mSubject;

    @Before
    public void setUp() throws Exception {
//        SoundViewModel viewModel = new SoundViewModel(new BeatBox());
        mBeatBox = mock(BeatBox.class);
        mSound = new Sound("assetPath");
        mSubject = new SoundViewModel(mBeatBox);
        mSubject.setSound(mSound);

    }

    @Test
    public void exposesSoundNameAsTitle() {// 실제 테스트 코드
        assertThat(mSubject.getTitle(), is(mSound.getName()));//mSubject의 타이틀이 우리가 만든 사운드의 이름하고 같다 를 테스트 ?
        //나중에 프로그램 고쳤는데, 사운드파일 이름 조작하는 부분을 고쳤을때, 잘못 고치면 이 두개가 다르게 나오므로 테스트가 실패 - > 코드 수정에 도움되는?
    }

    @Test
    public void callsBeatBoxPlayOnButtonClicked() {
        mSubject.onButtonClick();//이 메소드가 실행되었을때
        verify(mBeatBox).play(mSound);//verify메소드의 결과가 나와야한다.( mBeatBox.play(mSound)가 작동해야한다.)
        //결론적으로 코드가 어떤 수정을 거치든 간에 이 테스트 메소드의 과정을 거쳤을때 verify의 내용이 실행되어야 한다는뜻.
    }


}