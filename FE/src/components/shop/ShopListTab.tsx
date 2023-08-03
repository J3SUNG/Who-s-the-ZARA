import { ShopListTabItem } from "./ShopListTabItem";
import backImg from "../../assets/img/shop/backImg.png";
import { Link } from "react-router-dom";

interface ShopListTabProps {
  selectTab: number;
  setSelectTab: (num: number) => void;
}

export const ShopListTab = ({ selectTab, setSelectTab }: ShopListTabProps) => {
  const tabList = [0, 1, 2];
  return (
    <div className="flex w-[100%] justify-center 3xl:my-[10px] my-[8px]">
      {tabList.map((item) => (
        <ShopListTabItem index={item} selectTab={selectTab} setSelectTab={setSelectTab} key={item} />
      ))}
      <Link to="/lobby" className="3xl:w-[120px] w-[96px] 3xl:mr-[90px] mr-[72px]">
        <div
          className={`bg-gray-700 3xl:w-[120px] w-[96px] 3xl:h-[100px] h-[80px] flex justify-center items-center cursor-pointer rounded-2xl 3xl:mx-[40px] mx-[32px] border-solid border-white 3xl:border-[8px] border-[6.4px] shadow-black shadow-lg"
        }`}
        >
          <img src={backImg} className="w-full 3xl:p-[20px] p-[16px]" />
        </div>
      </Link>
    </div>
  );
};