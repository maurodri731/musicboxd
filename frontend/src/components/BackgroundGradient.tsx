// BackgroundGradient.tsx
import { motion } from 'framer-motion';

interface Props {
  gradient: string;
}

export default function BackgroundGradient({ gradient }: Props) {
  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 100 }}
      transition={{ duration: 1.0 }}
      className="fixed inset-0 -z-10"
      style={{ background: gradient }}
    />
  );
}