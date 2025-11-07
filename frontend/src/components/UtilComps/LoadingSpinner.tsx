// Loading Spinner Component
type SpinnerSize = 'sm' | 'md' | 'lg';
type SpinnerColor = 'blue' | 'red' | 'green' | 'purple';

interface LoadingSpinerProps {
  size?: SpinnerSize;
  color?: SpinnerColor;
}
export const LoadingSpinner = ({ size = 'md', color = 'blue' }: LoadingSpinerProps) => {
  const sizeClasses: Record<SpinnerSize, string> = {
    sm: 'w-6 h-6 border-2',
    md: 'w-12 h-12 border-4',
    lg: 'w-16 h-16 border-4',
  };

  const colorClasses: Record<SpinnerColor, string> = {
    blue: 'border-blue-500',
    red: 'border-red-500',
    green: 'border-green-500',
    purple: 'border-purple-500',
  };

  return (
    <div
      className={`${sizeClasses[size]} border-gray-200 ${colorClasses[color]} border-t-transparent rounded-full animate-spin`}
    />
  );
};