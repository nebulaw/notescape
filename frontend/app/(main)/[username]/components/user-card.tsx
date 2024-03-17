import { UserType } from "@/types/auth";
import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Image from "next/image";
import { instance as axios } from "@/app/lib/data";
import { useUser } from "@/hooks/use-user";
import LoadingScreen from "@/app/components/loading-screen";
import { toast } from "react-toastify";

interface Props {
  username: string
}

export default function Card({ username }: Props) {
  const [ isFollowing, setIsFollowing ] = useState(false);
  const { user: authUser, status: authStatus } = useUser();
  const [ status, setStatus ] = useState<'loading' | 'loaded' | 'error' | 'pending' | 'invalid'>('loading');
  const [ user, setUser ] = useState<UserType | null>(null);
  const isMe = authUser?.user.username === username;
  const router = useRouter();

  const handleFollow = () => {
    setIsFollowing(true);
    let followingCount = user ? user.followingCount : 0;
    setUser(prev => prev && ({
      ...prev,
      followerCount: followingCount + 1
    }));
    axios.post(`/interactions/follow?followerId=${authUser?.user.id}&followingId=${user?.id}`)
      .then(res => res.data)
      .then(data => {
        toast(data.data);
      })
  };

  const handleUnfollow = () => {
    setIsFollowing(false);
    let followerCount = user ? user.followerCount : 0;
    setUser(prev => prev && ({
      ...prev,
      followerCount: followerCount == 0 ? 0 : followerCount - 1
    }));
    axios.delete(`/interactions/unfollow?followerId=${authUser?.user.id}&followingId=${user?.id}`)
      .then(res => res.data)
      .then(data => {
        toast(data.data)
      })
  };

  if (status === 'pending' && authUser?.user && user) {
    (() => {
      axios.get(`/interactions/relation-type?user1Id=${authUser?.user.id}&user2Id=${user.id}`)
        .then((res) => res.data)
        .then(res => {
          if (res.data) {
            if (res.data === "FOLLOWING" || res.data === "FRIENDS") {
              setIsFollowing(true);
            }
          }
        })
    })();
    setStatus('loaded');
  }

  useEffect(() => {
    setStatus('loading');
    axios.get(`/users/find/${username}`)
      .then((res) => {
        if (res.status === 200 && res.data.data) {
          setUser(res.data.data);
        } else {
          // TODO: better error handling
          setStatus('invalid');
        }
      })
      .catch((err) => {
        // TODO: error handling
        setStatus('error');
        console.log(err);
      });
    setStatus('pending');
  }, []);

  if (status === 'loading') {
    return <LoadingScreen fullScreen />
  }

  return <div className="flex flex-col w-full bg-transparent pt-8 pb-2 px-4">
    <div className="flex gap-6 items-start justify-start">
      <Image
        className="aspect-square h-32 w-32 rounded-full"
        src="/abstract.png"
        alt="image"
        priority
        width={120}
        height={0}
      />
      <div className="flex flex-col pt-3">
        <p className="text-3xl font-bold">
          {user?.fullName}
        </p>
        <p className="font-semibold mb-3">
          @{user?.username}
        </p>
        <div className="flex gap-3">
          <a href=""><p>{`${user?.noteCount} posts`}</p></a>
          <a href=""><p>{`${user?.followerCount} followers`}</p></a>
          <a href=""><p>{`${user?.followingCount} followings`}</p></a>
        </div>
      </div>
    </div>
    <div className="text-left leading-3 my-6">
      {user?.about}
    </div>
    <div className="w-full">
      {
        isMe ? (
          <button className="w-full" onClick={() => router.push(`/${user?.username}/update`)}>
            Edit Profile
          </button>
        ) : (
          isFollowing ? (
            <button className="w-full" onClick={handleUnfollow}>
              Unfollow
            </button>
          ) : (
            <button
              className="w-full border-gray-800 hover:bg-white bg-white text-black"
              onClick={handleFollow}
            >
              Follow
            </button>
          )
        )
      }
    </div>
  </div>
}


