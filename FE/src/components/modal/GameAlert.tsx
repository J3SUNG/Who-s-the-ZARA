import { useState } from "react";
import Rodal from "rodal";
import { TEXT_COLOR_MAP } from "../../constants/common/TextColorMap";
import { NIGHT_RESULT_MAP } from "../../constants/game/NightResultMap";

interface GameAlertProps {
  alertType: number;
  userInfo: {
    userSeq: number;
    jobSeq: number;
    nickname: string;
  }[];
  deathByZaraOrderNo: number | null;
}
export const GameAlert = ({ alertType, userInfo, deathByZaraOrderNo }: GameAlertProps) => {
  const [viewMyJob, setViewMyJob] = useState(true);
  console.log(alertType);
  console.log(userInfo);
  console.log(deathByZaraOrderNo);
  return (
    <Rodal
      visible={viewMyJob}
      onClose={() => setViewMyJob(false)}
      enterAnimation="slideUp"
      leaveAnimation="slideDown"
      duration={500}
      width={1}
      height={1}
      closeOnEsc={true}
      showCloseButton={false}
      customStyles={{
        backgroundColor: "rgba(0, 0, 0, 0)",
      }}
    >
      <div className="absolute left-[50%] top-[50%] -translate-x-1/2 -translate-y-1/2 3xl:w-[480px] w-[384px] 3xl:h-[480px] h-[384px] bg-gray-900 border-solid 3xl:border-[10px] border-[8px] border-white flex flex-col justify-center items-center 3xl:text-[32px] text-white text-[25.6px]">
        {alertType === NIGHT_RESULT_MAP.DEATH && deathByZaraOrderNo !== null && (
          <p className="">
            밤 중에{" "}
            <span className={`${TEXT_COLOR_MAP[deathByZaraOrderNo]}`}>{userInfo[deathByZaraOrderNo].nickname}</span>{" "}
            님이 공격 당했습니다...
          </p>
        )}
        {alertType === NIGHT_RESULT_MAP.SAFE && <p className="">평화로운 아침이 밝았습니다.</p>}
        {alertType === NIGHT_RESULT_MAP.TARGET && (
          <p className="text-red-300">당신은 자라의 공격을 받아서 사망했습니다...</p>
        )}
      </div>
    </Rodal>
  );
};
