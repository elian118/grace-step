import React from 'react';
import { useDialog } from '@/hooks/useDialog';
import { useToast } from '@/hooks/useToast';

const Home = () => {
  const { open, closeByKey } = useDialog();
  const { toast } = useToast();

  const handleOpenSimple = () => {
    open('SAMPLE_DIALOG', {
      title: '일반 알림 다이얼로그',
      props: {
        message: '안녕하세요! 이것은 전역 다이얼로그 시스템의 기본적인 호출 방식 예시입니다.',
        onConfirm: () => {
          alert('확인이 클릭되었습니다!');
          closeByKey('SAMPLE_DIALOG');
        },
        onCancel: () => {
          closeByKey('SAMPLE_DIALOG');
        },
      },
    });
  };

  const handleOpenDragAndBlur = () => {
    open('SAMPLE_DIALOG', {
      title: '드래그 & 블러 다이얼로그',
      props: {
        message: '제목 표시줄(헤더) 영역을 마우스로 잡고 드래그하여 다이얼로그 창의 위치를 자유롭게 이동시킬 수 있으며, 뒷배경에 아름답고 모던한 blur 효과가 은은하게 흐릅니다.',
        onConfirm: () => closeByKey('SAMPLE_DIALOG'),
      },
      options: {
        allowDrag: true,
        blurBackground: true,
      },
    });
  };

  const handleOpenPreventClose = () => {
    open('SAMPLE_DIALOG', {
      title: '안전 장치 활성 모달 창',
      props: {
        message: '이 모달 창은 잘못된 동작으로 닫히는 것을 방지하기 위해 ESC 키를 누르거나 어두운 배경 영역을 마우스 클릭하더라도 절대로 닫히지 않습니다. 명시적인 상호작용(확인, 취소, X 버튼)만 허용합니다.',
        onConfirm: () => closeByKey('SAMPLE_DIALOG'),
        onCancel: () => closeByKey('SAMPLE_DIALOG'),
      },
      options: {
        preventCloseOnEsc: true,
        preventCloseOnOutsideClick: true,
      },
    });
  };

  const handleOpenLarge = () => {
    open('SAMPLE_DIALOG', {
      title: '대용량 데이터 조회 (xl) 다이얼로그',
      props: {
        message: 'xl 크기의 넓고 쾌적한 공간입니다. 상세 목록, 차트 뷰, 혹은 방대한 입력 필드 그룹을 디자인할 때 레이아웃 왜곡 없이 매끄러운 사용자 경험을 선사합니다.',
        onConfirm: () => closeByKey('SAMPLE_DIALOG'),
      },
      options: {
        size: 'xl',
      },
    });
  };

  const handleOpenFallback = () => {
    open('UNKNOWN_DIALOG_KEY', {
      title: '정의되지 않은 모달 호출',
      props: {
        message: '이 내용은 레지스트리에 키가 등록되어 있지 않은 모달을 열었을 때 어떻게 에러를 방지하고 피드백하는지 보여줍니다.',
      },
    });
  };

  return (
    <div className="p-8 space-y-6 max-w-4xl mx-auto">
      <div className="card bg-base-100 shadow-xl border border-base-200">
        <div className="card-body">
          <h2 className="card-title text-2xl font-bold text-primary mb-2">
            Grace Step 전역 다이얼로그 대시보드
          </h2>
          <p className="text-base-content/70 text-sm mb-6 leading-relaxed">
            리덕스 Toolkit 전역 상태 및 DaisyUI 5를 기반으로 구축된 고기능성 전역 다이얼로그 솔루션입니다.<br />
            컴포넌트 호출 없이 단 하나의 <code className="bg-base-200 p-1 rounded font-mono text-secondary">useDialog</code> 훅만으로 드래그앤드롭 위치 이동, 배경 블러, 레이아웃 차단, 11단계 사이즈 구성 및 Fallback UI까지 정교한 세부 제어가 가능합니다.
          </p>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <button className="btn btn-primary btn-md shadow-md" onClick={handleOpenSimple}>
              기본 다이얼로그 테스트
            </button>
            <button className="btn btn-secondary btn-md shadow-md" onClick={handleOpenDragAndBlur}>
              드래그 허용 + 배경 블러
            </button>
            <button className="btn btn-accent btn-md shadow-md text-white" onClick={handleOpenPreventClose}>
              ESC / 외부 클릭 자동 차단
            </button>
            <button className="btn btn-info btn-md shadow-md text-white" onClick={handleOpenLarge}>
              대용량용 (xl) 사이즈 모달
            </button>
            <button className="btn btn-error btn-outline btn-md col-span-1 md:col-span-2 shadow-sm" onClick={handleOpenFallback}>
              등록되지 않은 키 호출 (Fallback UI 가이드 검증)
            </button>
          </div>
        </div>
      </div>

      <div className="card bg-base-100 shadow-xl border border-base-200">
        <div className="card-body">
          <h2 className="card-title text-2xl font-bold text-secondary mb-4">
            Grace Step 전역 토스트 메시지
          </h2>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <button className="btn btn-info text-white" onClick={() => toast('정보를 알려드립니다.', 'info')}>Info 토스트</button>
            <button className="btn btn-success text-white" onClick={() => toast('작업이 완료되었습니다!', 'success')}>Success 토스트</button>
            <button className="btn btn-warning text-white" onClick={() => toast('주의가 필요합니다.', 'warning')}>Warning 토스트</button>
            <button className="btn btn-error text-white" onClick={() => toast('에러가 발생했습니다.', 'error')}>Error 토스트</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
