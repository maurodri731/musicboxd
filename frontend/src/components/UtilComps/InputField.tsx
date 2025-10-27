import { useState } from "react";

interface Props {
  type: string;
  placeholder: string;
  icon: string;
  value: string;
  onChange: (value: string) => void;
  name: string;
  addStyle: boolean;
}

const InputField = ({ type, placeholder, icon, value, onChange, name, addStyle }: Props) => {
  const [isPasswordShown, setIsPasswordShown] = useState(false);
  
  return (
    <div className={`relative h-14 mb-6 ${addStyle ? 'w-full' : 'w-full'}`}>
      <input
        type={isPasswordShown ? 'text' : type}
        placeholder={placeholder}
        className="w-full h-full text-lg rounded border border-blue-400 px-5 pl-12 outline-none focus:border-indigo-900 transition-colors placeholder:text-slate-500"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        name={name}
        required
      />
      
      {icon && (
        <i className="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 flex items-center h-full text-slate-500 pointer-events-none">
          {icon}
        </i>
      )}
      
      {type === 'password' && value && (
        <i 
          onClick={() => setIsPasswordShown(prevState => !prevState)} 
          className="material-symbols-outlined absolute right-4 top-1/2 -translate-y-1/2 flex items-center text-slate-500 cursor-pointer text-xl"
        >
          {isPasswordShown ? 'visibility' : 'visibility_off'}
        </i>
      )}
    </div>
  );
};

export default InputField;